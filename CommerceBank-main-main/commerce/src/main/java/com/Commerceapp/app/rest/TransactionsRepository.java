package com.Commerceapp.app.rest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface TransactionsRepository extends JpaRepository<Transactions, Long> {

    @Query("SELECT u FROM Transactions u WHERE u.fromSource = ?1")
    List<Transactions> findByFromSource(String fromSource);

    @Query("SELECT u FROM Transactions u WHERE u.toSource = ?1")
    List<Transactions> findByToSource(String toSource);

    @Query("SELECT u FROM Transactions u WHERE u.fromSource = ?1 AND u.category = 'Direct Deposit'")
    List<Transactions> findByDeposit(String fromSource);


    @Query("SELECT u FROM Transactions u WHERE u.fromSource = ?1 AND u.category = 'Rent'")
    List<Transactions> findByRent(String fromSource);

    @Query("SELECT u FROM Transactions u WHERE u.fromSource = ?1 AND u.category = 'Bills'")
    List<Transactions> findByBills(String fromSource);

    @Query("SELECT u FROM Transactions u WHERE u.fromSource = ?1 AND u.category = 'Transportation'")
    List<Transactions> findByTrans(String fromSource);

    @Query("SELECT u FROM Transactions u WHERE u.fromSource = ?1 AND u.category = 'Misc'")
    List<Transactions> findByMisc(String fromSource);

    @Query("SELECT u FROM Transactions u WHERE u.fromSource = ?1 AND u.category = 'Foods'")
    List<Transactions> findByFood(String fromSource);



/*

    @Query("SELECT u FROM Transactions u WHERE u.fromSource = ?1 AND u.category = :category")
    List<Transactions> findByRent(String fromSource, String category);
*/

//    @Query("SELECT u FROM Transactions u WHERE u.fromSource = ?1 AND u.category = ")
//    List<Transactions> findByRent(String fromSource, String category);

}
