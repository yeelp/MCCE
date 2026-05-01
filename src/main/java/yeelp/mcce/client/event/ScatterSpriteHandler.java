package yeelp.mcce.client.event;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import yeelp.mcce.MCCE;
import yeelp.mcce.client.event.ClientRenderCallbacks.OnSpriteDrawCallback;
import yeelp.mcce.event.CallbackResult;
import yeelp.mcce.model.chaoseffects.ScatterEffect;
import yeelp.mcce.util.ChaosLib;

import java.util.*;

public final class ScatterSpriteHandler implements OnSpriteDrawCallback {
    private static final Set<RenderPipeline> AFFECTED_PIPELINES = Sets.newHashSet(RenderPipelines.GUI, RenderPipelines.GUI_TEXTURED);
    private static final Map<UUID, Pair<ScatterIterator, ScatterInfo>> TRACKED = Maps.newHashMap();
    private static final int PAD = 70;
    private static final String CONTAINER_REGEX = ".*(container|empty).*";

    @Override
    public CallbackResult shouldDraw(DrawContext context, RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        IconType.getIconType(sprite).ifPresent((icon) -> {
            boolean isContainer = sprite.toString().matches(CONTAINER_REGEX);
            if(player != null && ScatterEffect.isClientTracked(player) && AFFECTED_PIPELINES.contains(pipeline)) {
                Pair<ScatterIterator, ScatterInfo> p = TRACKED.get(player.getUuid());
                ScatterIterator it = p.getLeft();
                ScatterInfo info = p.getRight();
                int[] delta;
                int indexUsed;
                it.inc(icon, isContainer, context, x);
                delta = p.getRight().getOffset(icon, indexUsed = it.get(icon));
                int[] coords = new int[]{x, y};
                int[] screen = new int[]{MinecraftClient.getInstance().getWindow().getScaledWidth(), MinecraftClient.getInstance().getWindow().getScaledHeight()};
                boolean shouldUpdate = false;
                for(int i = 0; i < coords.length; i++) {
                    while(coords[i] + delta[i] < PAD) {
                        delta[i] += PAD;
                        shouldUpdate = true;
                    }
                    while(coords[i] + delta[i] > screen[i] - PAD) {
                        delta[i] -= PAD;
                        shouldUpdate = true;
                    }
                }
                if(shouldUpdate) {
                    info.update(icon, indexUsed, delta);
                }
                context.getMatrices().translate(delta[0], delta[1]);
            }

        });
        return new CallbackResult();
    }

    public static void addPlayer(PlayerEntity player) {
        TRACKED.put(player.getUuid(), new Pair<>(new ScatterIterator(), new ScatterInfo()));
    }

    public static void removePlayer(PlayerEntity player) {
        TRACKED.remove(player.getUuid());
    }

    public static void reset(PlayerEntity player) {
        TRACKED.get(player.getUuid()).getLeft().reset();
    }

    @Override
    public int priority() {
        return Integer.MIN_VALUE;
    }

    private enum IconType {
        HEART("heart", true) {
            @Override
            int[][] getOffsets(ScatterInfo info) {
                return new int[0][];
            }
        },
        HUNGER("food", true) {
            @Override
            int[][] getOffsets(ScatterInfo info) {
                return info.hungerOffsets();
            }
        },
        AIR("air", false) {
            @Override
            int[][] getOffsets(ScatterInfo info) {
                return info.airOffsets();
            }
        },
        ARMOR("armor", false) {
            @Override
            int[][] getOffsets(ScatterInfo info) {
                return info.armorOffsets();
            }
        };

        private final String spriteSubstring;
        private final boolean hasSeparateContainers;

        IconType(String spriteSubstring, boolean hasSeparateContainers) {
            this.spriteSubstring = spriteSubstring;
            this.hasSeparateContainers = hasSeparateContainers;
        }

        static Optional<IconType> getIconType(Identifier sprite) {
            String spriteId = sprite.toString();
            for(IconType type : IconType.values()) {
                if(spriteId.contains(type.spriteSubstring)) {
                    return Optional.of(type);
                }
            }
            return Optional.empty();
        }

        boolean hasSeparateContainers() {
            return this.hasSeparateContainers;
        }

        abstract int[][] getOffsets(ScatterInfo info);
    }

    private record ScatterInfo(List<Pair<Integer, Integer>> heartOffsets, int[][] hungerOffsets, int[][] armorOffsets, int[][] airOffsets) {
        private static final int X_PAD = 70, Y_OFFSET_MAX = 20, Y_OFFSET_MIN = -200;
        ScatterInfo() {
            this(Lists.newLinkedList(), new int[10][2], new int[10][2], new int[10][2]);
            this.initializeArray(this.hungerOffsets);
            this.initializeArray(this.airOffsets);
            this.initializeArray(this.armorOffsets);
        }

        private void initializeArray(int[][] arr) {
            for(int i = 0; i < arr.length; i++) {
                for(int j = 0; j < arr[0].length; j++) {
                    arr[i][j] = j == 0 ? getRandomX() : getRandomY();
                }
            }
        }

        int[] getOffset(IconType type, int index) {
            if(type == IconType.HEART) {
                return this.getHeartOffset(index);
            }
            return type.getOffsets(this)[index];
        }

        private int[] getHeartOffset(int heartIndex) {
            while(heartIndex >= this.heartOffsets.size()) {
                Pair<Integer, Integer> p = new Pair<>(getRandomX(), getRandomY());
                this.heartOffsets.add(p);
            }
            Pair<Integer, Integer> p = this.heartOffsets.get(heartIndex);
            return new int[] {p.getLeft(), p.getRight()};
        }

        private static int getRandomX() {
            return ChaosLib.getStaticRandomInstance().nextInt(-X_PAD, X_PAD);
        }

        private static int getRandomY() {
            return ChaosLib.getStaticRandomInstance().nextInt(Y_OFFSET_MIN, Y_OFFSET_MAX);
        }

        void update(IconType type, int index, int[] data) {
            if(type == IconType.HEART) {
                Pair<Integer, Integer> p = this.heartOffsets.get(index);
                p.setLeft(data[0]);
                p.setRight(data[1]);
            }
            else {
                type.getOffsets(this)[index] = data;
            }
        }
    }

    private static final class ScatterIterator {
        private final Map<IconType, Integer> iterators = new EnumMap<>(IconType.class);
        private int bubbleCount;

        @SuppressWarnings("MagicNumber")
        void inc(IconType type, boolean isContainer, DrawContext context, int xPos) {
            int inc = 1;
            if(type == IconType.AIR) {
                int left = context.getScaledWindowWidth() / 2 + 91;
                int bubbleNo = ((left - 9 - xPos) / 8) + 1;
                inc = Math.max(1, bubbleNo - this.bubbleCount);
                if(inc != 1) {
                    MCCE.LOGGER.info(""+inc);
                }
                this.bubbleCount = bubbleNo;
            }
            if(!type.hasSeparateContainers() || isContainer) {
                this.iterators.merge(type, inc, Integer::sum);
            }
        }

        int get(IconType type) {
            return this.iterators.get(type);
        }

        void reset() {
            Arrays.stream(IconType.values()).forEach((icon) -> this.iterators.put(icon, -1));
            this.bubbleCount = 0;
        }
    }
}
