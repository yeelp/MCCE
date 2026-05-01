package yeelp.mcce.client.event;

import com.google.common.collect.Iterators;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import yeelp.mcce.event.CallbackResult;
import yeelp.mcce.event.CallbackResult.ProcessState;

import java.util.Arrays;
import java.util.Iterator;

public interface ClientRenderCallbacks {

	@FunctionalInterface
	interface BeforeClientRenderCallback {

		Event<BeforeClientRenderCallback> EVENT = EventFactory.createArrayBacked(BeforeClientRenderCallback.class, (listeners) -> () -> Iterators.forArray(listeners).forEachRemaining(BeforeClientRenderCallback::beforeClientRender));

		void beforeClientRender();
	}

	@FunctionalInterface
	interface AfterClientRenderCallback {

		Event<AfterClientRenderCallback> EVENT = EventFactory.createArrayBacked(AfterClientRenderCallback.class, (listeners) -> () -> Iterators.forArray(listeners).forEachRemaining(AfterClientRenderCallback::afterClientRender));

		void afterClientRender();
	}

	@FunctionalInterface
	interface ChangeSpriteColourCallback {

		Event<ChangeSpriteColourCallback> EVENT = EventFactory.createArrayBacked(ChangeSpriteColourCallback.class, (listeners) -> (pipeline, sprite, colour) -> {
			Iterator<ChangeSpriteColourCallback> it = Iterators.forArray(listeners);
			for(; it.hasNext(); colour = it.next().changeColour(pipeline, sprite, colour));
			return colour;
		});

		int changeColour(RenderPipeline pipeline, Identifier sprite, int currentColour);
	}

	interface ChangeSpriteCallback {
		Event<ChangeSpriteCallback> EVENT = EventFactory.createArrayBacked(ChangeSpriteCallback.class, (listeners -> (id, pipeline) -> {
			Iterator<ChangeSpriteCallback> it = Iterators.forArray(listeners);
			for(; it.hasNext(); id = it.next().getNewSprite(id, pipeline));
			return id;
		}));

		Identifier getNewSprite(Identifier sprite, RenderPipeline pipeline);
	}

	@FunctionalInterface
	interface OnCameraUpdate {

		Event<OnCameraUpdate> EVENT = EventFactory.createArrayBacked(OnCameraUpdate.class, (listeners) -> (camera) -> Iterators.forArray(listeners).forEachRemaining((listener) -> listener.updateCamera(camera)));

		void updateCamera(Camera camera);
	}

	@FunctionalInterface
	interface OnSpriteDrawCallback extends Comparable<OnSpriteDrawCallback> {
		Event<OnSpriteDrawCallback> EVENT = EventFactory.createArrayBacked(OnSpriteDrawCallback.class, (listeners) -> (context, pipeline, sprite, x, y, width, height) -> {
			Iterator<OnSpriteDrawCallback> it = Arrays.stream(listeners).sorted().iterator();
			CallbackResult result = new CallbackResult();
			for(; result.getProcessState() == ProcessState.PASS && it.hasNext(); result = result.mergeResults(it.next().shouldDraw(context, pipeline, sprite, x, y, width, height)));
			return result;
		});

		default int priority() {
			return 0;
		}

		default int compareTo(OnSpriteDrawCallback other) {
			return -(this.priority() - other.priority());
		}

		CallbackResult shouldDraw(DrawContext context, RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height);
	}

	@FunctionalInterface
	interface SetShaderCallback {
		Event<SetShaderCallback> EVENT = EventFactory.createArrayBacked(SetShaderCallback.class, (listeners) -> (curr) -> {
			Iterator<SetShaderCallback> it = Iterators.forArray(listeners);
			Identifier id = curr;
			for(; it.hasNext(); id = it.next().setShaderIdentifier(id));
			return id;
		});

		Identifier setShaderIdentifier(Identifier curr);
	}

	@FunctionalInterface
	interface BeforeModelRenderCallback {
		Event<BeforeModelRenderCallback> EVENT = EventFactory.createArrayBacked(BeforeModelRenderCallback.class, (listeners) -> (stack, model) -> Iterators.forArray(listeners).forEachRemaining((listener) -> listener.beforeRender(stack, model)));

		void beforeRender(MatrixStack stack, Model<?> model);
	}
}
