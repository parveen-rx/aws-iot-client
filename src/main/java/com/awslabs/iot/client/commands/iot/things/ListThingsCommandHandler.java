package com.awslabs.iot.client.commands.iot.things;

import com.amazonaws.services.iot.model.ThingAttribute;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.helpers.io.interfaces.IOHelper;
import com.awslabs.iot.client.helpers.iot.interfaces.ThingHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.Optional;

public class ListThingsCommandHandler implements IotCommandHandler {
    private static final String LISTTHINGS = "list-things";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ListThingsCommandHandler.class);
    @Inject
    Provider<ThingHelper> thingHelperProvider;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IOHelper ioHelper;

    @Inject
    public ListThingsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        List<ThingAttribute> thingList = thingHelperProvider.get().listThingAttributes();

        for (ThingAttribute thingAttribute : thingList) {
            Optional<String> optionalThingTypeName = Optional.ofNullable(thingAttribute.getThingTypeName());
            log.info("  [" + thingAttribute.getThingName() + "] [" + optionalThingTypeName.orElse("NO THING TYPE") + "]");
        }
    }

    @Override
    public String getCommandString() {
        return LISTTHINGS;
    }

    @Override
    public String getHelp() {
        return "Lists all things.";
    }

    @Override
    public int requiredParameters() {
        return 0;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public IOHelper getIoHelper() {
        return this.ioHelper;
    }
}