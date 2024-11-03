/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consolebasedbankingapplication.Service;

import consolebasedbankingapplication.Entity.User;
import consolebasedbankingapplication.Repository.UserRepository;

import java.util.List;
import java.util.Map;


public class UserService {
    private UserRepository userRepository=new UserRepository();
    
    public void printUsers(){
        userRepository.printUsers();
    }
    
    public User login(String username, String password){
        return userRepository.login(username, password);
    }

    public boolean addNewCustomer(String username, String password, String contact){
        return userRepository.addNewCustomer(username,password,contact);
    }
    public Double checkBankBalance(String userId){
        return userRepository.checkBankBalance(userId);
    }
    public User getUser(String userId){
        return userRepository.getUser(userId);
    }
    public boolean transferAmount(String userId, String payeeUserId, Double amount){
        return userRepository.transferAmount(userId, payeeUserId, amount);
    }
    public void printTransactions(String userId){
        userRepository.printTransactions(userId);
    }
    public void raiseChequeBookRequest(String userId){
        userRepository.raiseChequeBookRequest(userId);
    }
    public Map<String, Boolean> getAllChequeBookRequest(){
        return userRepository.getAllChequeBookRequest();
    }
    public List<String> getUserIdForChequeBookRequest(){
        return userRepository.getUserIdForChequeBookRequest();
    }
    public void approveChequeBookRequest(String userId){
        userRepository.approveChequeBookRequest(userId);
    }
}
