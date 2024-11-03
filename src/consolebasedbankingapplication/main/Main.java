/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consolebasedbankingapplication.main;

import consolebasedbankingapplication.Entity.User;
import consolebasedbankingapplication.Service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Ahmed Shaltout
 */
public class Main {
    
    private static Scanner sc = new Scanner(System.in);
    private static UserService userService=new UserService();

    public static void main(String[] args) {

        while(true){
            System.out.println("Welcome to login Page");
            System.out.println("Eneter your username: ");
            String username=sc.next();

            System.out.println("Entere your password: ");
            String password=sc.next();

            User user = userService.login(username, password);

            if(user != null && user.getRole().equals("admin")){
                initAdmin();
            }else if(user != null && user.getRole().equals("user")){
                initCustomer(user);
            }
            else{
                System.out.println("Login failed");
            }
        }
        
    }
    private static void initAdmin(){
        boolean flag = true;
        String userId="";

        while(flag){
            System.out.println("1. Exit/Logout");
            System.out.println("2. Create a Customer Account");
            System.out.println("3. See all transactions");
            System.out.println("4. Check Bank Balance");
            System.out.println("5. Approve cheque book request");

            int selectedOption = sc.nextInt();
            switch(selectedOption){
                case 1:
                    System.out.println("You have Successfully logged out..");
                    flag = false;
                    break;
                case 2:
                    addNewCustomer();
                    break;
                case 3:
                    System.out.println("Enter user id:");
                    userId=sc.next();
                    printTransactions(userId);
                    break;
                case 4:
                    System.out.println("Enter user id:");
                    userId=sc.next();
                    Double accountBalance =  checkBankBalance(userId);
                    System.out.println("Your account balance is "+accountBalance);
                    break;
                case 5:
                    List<String> userIds=getUserIdForChequeBookRequest();
                    System.out.println("Please Select user id from below..");
                    System.out.println(userIds);
                    userId=sc.next();
                    approveChequeBookRequest(userId);
                    System.out.println("Cheque book request is approved");
                    break;
                default:
                    System.out.println("Wrong Choice");
            }
        }
    }
    private static void approveChequeBookRequest(String userId){
        userService.approveChequeBookRequest(userId);
    }
    private static void addNewCustomer(){
        System.out.println("Enter your username: ");
        String username=sc.next();
        
        System.out.println("Enter your password: ");
        String password=sc.next();
        
        System.out.println("Enter your contact number");
        String contactNumber=sc.next();

        boolean result = userService.addNewCustomer(username, password, contactNumber);

        if(result){
            System.out.println("customer account have been created ...");
        }else{
            System.out.println("customer account creation failed ...");
        }

    }
    private static void initCustomer(User user){
        boolean flag=true;


        while(flag){
            System.out.println("1. Exit/LogOut");
            System.out.println("2. Check Account Balance");
            System.out.println("3. Fund transfer balance");
            System.out.println("4. See all transactions");
            System.out.println("5. Raise cheque book request");

            int selectedOption=sc.nextInt();

            switch(selectedOption){
                case 1:
                    System.out.println("You have Successfully Logged out ...");
                    flag=false;
                    break;
                case 2:
                    Double balance=checkBankBalance(user.getUsername());
                    if(balance!=null){
                        System.out.println("Your Account Balance is "+balance);
                    }else{
                        System.out.println("Check Your username");
                    }
                    break;
                case 3:
                    fundTransfer(user);
                    break;
                case 4:
                    printTransactions(user.getUsername());
                    break;
                case 5:
                    String userId=user.getUsername();
                    Map<String, Boolean> map = getAllChequeBookRequest();
                    if(map.containsKey(userId) && map.get(userId)){
                        System.out.println("You have already raised a request and it is already approved");
                    }else if(map.containsKey(userId) && !map.get(userId)){
                        System.out.println("You have already raised a request and it is pending for approval");
                    }else{
                        raiseChequeBookRequest(userId);
                        System.out.println("Request raised successfully..");
                    }
                    break;

                default:
                    System.out.println("Wrong Choice");
                    break;
            }
        }
    }
    private static List<String> getUserIdForChequeBookRequest(){
        return userService.getUserIdForChequeBookRequest();
    }
    private static Map<String, Boolean> getAllChequeBookRequest(){
        return userService.getAllChequeBookRequest();
    }

    private static void raiseChequeBookRequest(String userId){
        userService.raiseChequeBookRequest(userId);
    }
    private static void printTransactions(String userId){
        userService.printTransactions(userId);
    }
    private static void fundTransfer(User userDetails){
        System.out.println("Enter payee account user id");
        String payeeAccountId=sc.next();

        User user=getUser(payeeAccountId);
        if(user != null){
            System.out.println("Enter amount to transfer");
            Double amount=sc.nextDouble();
            Double userAccountBalance = checkBankBalance(userDetails.getUsername());
            if(userAccountBalance >= amount){
                boolean result = userService.transferAmount(userDetails.getUsername(), payeeAccountId, amount);

                if(result){
                    System.out.println("Amount transefered successfully ...");
                }else{
                    System.out.println("Transfer failed ...");
                }
            }else{
                System.out.println("Your balance is insufficient: "+userAccountBalance);
            }

        }else{
            System.out.println("please, enter valid username");
        }
    }
    private static Double checkBankBalance(String userId){
        return userService.checkBankBalance(userId);
    }
    private static User getUser(String userId){
        return userService.getUser(userId);
    }
}
