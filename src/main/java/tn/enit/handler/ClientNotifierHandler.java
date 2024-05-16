package tn.enit.handler;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import tn.enit.service.ClientNotifier;

import java.util.HashMap;
import java.util.Map;

public class ClientNotifierHandler implements JobHandler {

    ClientNotifier clientNotifierService = new ClientNotifier();

    @Override
    public void handle(JobClient client, ActivatedJob job) throws Exception {
        final Map<String, Object> inputVariables = job.getVariablesAsMap();
        final String num_carte_de_credit = (String) inputVariables.get("num_carte_de_credit");
        final String confirmation= clientNotifierService.notifierAssure(num_carte_de_credit);

        final Map<String, Object> outputVariables = new HashMap<String, Object>();
        outputVariables.put("confirmation", confirmation);
        //Envoyer la variable confirmation à partir du Job
        client.newCompleteCommand(job.getKey()).variables(outputVariables).send().join();
    }
}