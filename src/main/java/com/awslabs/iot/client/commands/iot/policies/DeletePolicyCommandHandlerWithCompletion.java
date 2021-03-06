package com.awslabs.iot.client.commands.iot.policies;

import com.awslabs.aws.iot.resultsiterator.helpers.interfaces.IoHelper;
import com.awslabs.aws.iot.resultsiterator.helpers.v1.interfaces.V1PolicyHelper;
import com.awslabs.iot.client.commands.iot.PolicyCommandHandlerWithCompletion;
import com.awslabs.iot.client.commands.iot.completers.PolicyCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

public class DeletePolicyCommandHandlerWithCompletion implements PolicyCommandHandlerWithCompletion {
    private static final String DELETEPOLICY = "delete-policy";
    private static final int POLICY_NAME_POSITION = 0;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DeletePolicyCommandHandlerWithCompletion.class);
    @Inject
    Provider<V1PolicyHelper> policyHelperProvider;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    PolicyCompleter policyCompleter;

    @Inject
    public DeletePolicyCommandHandlerWithCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        String policyName = parameters.get(POLICY_NAME_POSITION);

        policyHelperProvider.get().deletePolicy(policyName);
    }

    @Override
    public String getCommandString() {
        return DELETEPOLICY;
    }

    @Override
    public String getHelp() {
        return "Deletes a policy by name.";
    }

    @Override
    public int requiredParameters() {
        return 1;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }

    public PolicyCompleter getPolicyCompleter() {
        return this.policyCompleter;
    }
}
