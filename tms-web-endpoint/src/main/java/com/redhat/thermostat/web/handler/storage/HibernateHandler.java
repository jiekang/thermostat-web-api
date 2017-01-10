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
                try {
                    tm.begin();
                    {

                        em = emf.createEntityManager();

                        em.flush();
                        em.close();
                    }
                    tm.commit();

                } catch (NotSupportedException | SystemException | HeuristicRollbackException | HeuristicMixedException | RollbackException e) {
                    e.printStackTrace();
                }

                asyncResponse.resume(Response.status(Response.Status.OK).entity("").build());
            }
        }).start();
    }

    @Override
    public void putAgent(final SecurityContext context, final AsyncResponse asyncResponse, String body) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EntityManager em;
                try {
                    tm.begin();
                    {

                        em = emf.createEntityManager();



                        em.flush();
                        em.close();
                    }
                    tm.commit();

                } catch (NotSupportedException | SystemException | HeuristicRollbackException | HeuristicMixedException | RollbackException e) {
                    e.printStackTrace();
                }

                asyncResponse.resume(Response.status(Response.Status.OK).entity("").build());
            }
        }).start();
    }

    @Override
    public void test(final SecurityContext securityContext, final AsyncResponse asyncResponse) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String name = "";

                EntityManager em;
                try {
                    String id;

                    tm.begin();
                    {
                        TestItem t1 = new TestItem();
                        t1.setName("blob");

                        TestItem t2 = new TestItem();
                        t2.setName("blah");
                        em = emf.createEntityManager();

                        em.persist(t1);
                        em.persist(t2);

                        id = t1.getId();

                        em.flush();
                        em.close();
                    }
                    tm.commit();

                    tm.begin();
                    {
                        em = emf.createEntityManager();

                        name = em.find(TestItem.class, id).getName();

                        em.flush();
                        em.close();

                    }
                    tm.commit();

                } catch (NotSupportedException | SystemException | HeuristicRollbackException | HeuristicMixedException | RollbackException e) {
                    e.printStackTrace();
                }

                asyncResponse.resume(Response.status(Response.Status.OK).entity("Access! " + name).build());
            }
        }).start();
    }
}
