package tn.enit.service;

public class ClientNotifier {
    public String notifierAssure(final String num_carte_de_credit){
        //System.out.println("L'assuré ayant comme numéro de carte de crédit"+num_carte_de_credit+"a été notifié!");
        System.out.println("les informations sont validées!");

        final String confirmation = String.valueOf(System.currentTimeMillis());
        //System.out.println("Successful Transaction: " + confirmation);
        return confirmation;
    }
}