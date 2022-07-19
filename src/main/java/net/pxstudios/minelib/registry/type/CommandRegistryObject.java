package net.pxstudios.minelib.registry.type;

import net.pxstudios.minelib.command.type.AbstractContextCommand;
import net.pxstudios.minelib.registry.BukkitRegistryAdaptiveObject;

import java.lang.annotation.*;

public class CommandRegistryObject extends BukkitRegistryAdaptiveObject<AbstractContextCommand> {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SettingsMethod {
    }

    @Repeatable(MultipleAliases.class)
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Alias {

        String value();
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MultipleAliases {

        Alias[] value();
    }

}
