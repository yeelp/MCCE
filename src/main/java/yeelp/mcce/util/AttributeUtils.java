package yeelp.mcce.util;

import com.google.common.collect.Lists;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Objects;

public abstract class AttributeUtils {

    public static void addAttributeModifier(ItemStack stack, RegistryEntry<EntityAttribute> key, EntityAttributeModifier modifier, AttributeModifierSlot slot) {
        AttributeModifiersComponent comp = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        if (comp == null || comp == AttributeModifiersComponent.DEFAULT) {
            stack.set(DataComponentTypes.ATTRIBUTE_MODIFIERS, comp = new AttributeModifiersComponent(Lists.newArrayList()));
        }
        comp.modifiers().add(new AttributeModifiersComponent.Entry(key, modifier, slot));
    }

    public static void addAttributeModifier(LivingEntity entity, RegistryEntry<EntityAttribute> key, EntityAttributeModifier modifier) {
        Objects.requireNonNull(entity.getAttributeInstance(key)).addPersistentModifier(modifier);
    }

    public static void addAttributeModifierIfNotPresent(LivingEntity entity, RegistryEntry<EntityAttribute> key, EntityAttributeModifier modifier) {
        EntityAttributeInstance instance = Objects.requireNonNull(entity.getAttributeInstance(key));
        if (instance.hasModifier(modifier.id())) {
            return;
        }
        instance.addPersistentModifier(modifier);
    }
}
