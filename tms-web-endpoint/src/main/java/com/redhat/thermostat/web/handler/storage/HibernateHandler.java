package com.redhat.thermostat.web.handler.storage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.redhat.thermostat.entity.TestItem;

public class HibernateHandler implements StorageHandler {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("thermostat-web-endpoint");
    private final TransactionManager tm = com.arjuna.ats.jta.TransactionManager.transactionManager();


    @Override
    public void getAgent(final SecurityContext securityContext, final AsyncResponse asyncResponse, String agentId, String count, String sort) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EntityManager em;
                String id;
                try {
                    tm.begin();
                    {

                        em = emf.createEntityManager();
                        TestItem testItem = new TestItem();
                        testItem.setName("Blob");
                        em.persist(testItem);
                        id = testItem.getId();
                        em.flush();
                        em.close();

                    }
                    tm.commit();


                    tm.begin();
                    {
                        em = emf.createEntityManager();
                        TestItem itemTwo = new TestItem();
                        itemTwo.setName("Blah");
                        em.persist(itemTwo);
                        em.flush();
                        em.close();
                    }
                    tm.commit();

                    TestItem retrieved;
                    tm.begin();
                    {
                        em = emf.createEntityManager();
                        retrieved = em.find(TestItem.class, id);
                        em.flush();
                        em.close();
                    }
                    tm.commit();

                    System.out.println("Found: " + retrieved.getName() + " " + retrieved.getId());
                } catch (NotSupportedException | SystemException | HeuristicRollbackException | HeuristicMixedException | RollbackException e) {
                    e.printStackTrace();
                }

                asyncResponse.resume(Response.status(Response.Status.OK).entity("{ \"key\" : \"getAgent as: " + securityContext.getUserPrincipal().getName() + "\" }").build());
            }
        }).start();
    }
}
