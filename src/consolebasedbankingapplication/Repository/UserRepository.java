/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consolebasedbankingapplication.Repository;

import consolebasedbankingapplication.Entity.Transaction;
import consolebasedbankingapplication.Entity.User;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class UserRepository {
    private static Set<User> users=new HashSet<>();
    private static List<Transaction> transactions=new ArrayList<>();
    Map<String, Boolean> chequeBookRequest =new HashMap<>();

    static{
        User user1=new User("admin","admin","123456","admin",0.0);
        User user3=new User("user3","user3","1234569","user",2000.0);
        User user4=new User("user4","user4","1234569","user",1000.0);
        User user2=new User("user2","user2","1234568","user",1000.0);

        
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
    }
    public boolean transferAmount(String userId, String payeeUserId, Double amount){
        boolean isDebit=debit(userId, amount, payeeUserId);
        boolean isCredit=credit(payeeUserId, amount, userId);

        return isDebit && isCredit;
    }
    private boolean debit(String userId, Double amount, String payeeUserId){
        User user=getUser(userId);
        Double initBalance=user.getAccountBalance();
        Double finalBalance = initBalance-amount;

        users.remove(user);
        user.setAccountBalance(finalBalance);

        Transaction transaction = new Transaction(
                LocalDate.now(),
                payeeUserId,
                amount,
                "Debit",
                initBalance,
                finalBalance,
                userId
        );
        System.out.println(transaction);
        transactions.add(transaction);
        return users.add(user);
    }

    private boolean credit(String payeeUserId, Double amount, String userId){
        User user=getUser(payeeUserId);
        Double initBalance=user.getAccountBalance();
        Double finalBalance = initBalance+amount;

        users.remove(user);
        user.setAccountBalance(finalBalance);
        Transaction transaction = new Transaction(
                LocalDate.now(),
                userId,
                amount,
                "Credit",
                initBalance,
                finalBalance,
                payeeUserId
        );
        System.out.println(transaction);
        transactions.add(transaction);
        return users.add(user);
    }
    public void printTransactions(String userId){
        List<Transaction> filteredTransactions = transactions.stream().filter(transaction -> transaction.getTransactionPerformedBy().equals(userId)).collect(Collectors.toList());

        System.out.println("Date \t User Id \t Amount \t Type \t Initial Balance \t Final Balance");
        for(Transaction t:filteredTransactions){
            System.out.println(t.getTransactionDate()
            +" \t "+t.getTransactionUserId()
            +" \t "+t.getTransactionAmount()
            +" \t "+t.getTransactionType()
            +" \t "+t.getInitialBalance()
            +" \t "+t.getFinalBalance());
        }
        System.out.println("-------------------------------------------------");
    }
    public void printUsers(){
        System.out.println(users);
    }
    
    public User login(String username, String password){
        List<User> finalList = users.stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .collect(Collectors.toList());
        if(!finalList.isEmpty()){
            return finalList.get(0);
        }else{
            return null;
        }
    }

    public boolean addNewCustomer(String username, String password, String contact){
        User user = new User(username, password, contact, "user", 500.0);
        return users.add(user);
    }
    public Double checkBankBalance(String userId){
        List<User> filteredUsers = users.stream().filter(user -> user.getUsername().equals(userId))
                .collect(Collectors.toList());
        if(!filteredUsers.isEmpty()){
            return filteredUsers.get(0).getAccountBalance();
        }else{
            return null;
        }
    }

    public User getUser(String userId) {
        List<User> filteredUsers = users.stream()
                .filter(user -> user.getUsername().equals(userId))
                .collect(Collectors.toList());

        if (filteredUsers.isEmpty()) {
            return null;
        } else {
            return filteredUsers.get(0);
        }
    }
    public List<String> getUserIdForChequeBookRequest(){
        List<String> userIds=new ArrayList<>();
        for(Map.Entry<String, Boolean> entry: chequeBookRequest.entrySet()){
            if(!entry.getValue()){
                userIds.add(entry.getKey());
            }
        }
        return userIds;
    }
    public void approveChequeBookRequest(String userId){
        if(chequeBookRequest.containsKey(userId)){
            chequeBookRequest.put(userId, true);
        }
    }
    public void raiseChequeBookRequest(String userId){
        chequeBookRequest.put(userId, false);
    }
    public Map<String, Boolean> getAllChequeBookRequest(){
        return chequeBookRequest;
    }
}

