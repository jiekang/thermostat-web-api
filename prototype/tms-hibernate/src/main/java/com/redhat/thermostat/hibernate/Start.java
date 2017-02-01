package com.redhat.thermostat.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

public class Start {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(
                "thermostat-hibernate");
        TransactionManager tm = com.arjuna.ats.jta.TransactionManager.transactionManager();

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
    }
}
