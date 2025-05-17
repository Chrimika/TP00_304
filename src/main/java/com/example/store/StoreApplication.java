package com.example.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@SpringBootApplication
@RestController
@CrossOrigin
public class StoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreApplication.class, args);
        
        // Console application logic
        if (args.length > 0 && args[0].equals("console")) {
            runConsoleApp();
        }
    }
    
    private static void runConsoleApp() {
        Scanner scanner = new Scanner(System.in);
        Calculator calculator = new Calculator();
        boolean running = true;
        
        System.out.println("Console Calculator Started");
        
        while (running) {
            System.out.println("\nEnter operation (add, subtract, multiply, divide, exit): ");
            String operation = scanner.nextLine().trim().toLowerCase();
            
            if (operation.equals("exit")) {
                running = false;
                continue;
            }
            
            System.out.println("Enter first number: ");
            double num1;
            try {
                num1 = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number input");
                continue;
            }
            
            System.out.println("Enter second number: ");
            double num2;
            try {
                num2 = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number input");
                continue;
            }
            
            double result;
            try {
                switch (operation) {
                    case "add":
                        result = calculator.add(num1, num2);
                        break;
                    case "subtract":
                        result = calculator.subtract(num1, num2);
                        break;
                    case "multiply":
                        result = calculator.multiply(num1, num2);
                        break;
                    case "divide":
                        result = calculator.divide(num1, num2);
                        break;
                    default:
                        System.out.println("Unknown operation");
                        continue;
                }
                System.out.println("Result: " + result);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
        System.exit(0);
    }
    
    @GetMapping("/home")
    public String home() {
        return "Spring Boot";
    }
    
    @PostMapping("/calculate")
    public ResponseEntity<Map<String, Object>> calculate(@RequestBody CalculationRequest request) {
        Map<String, Object> response = new HashMap<>();
        Calculator calculator = new Calculator();
        
        try {
            double result;
            switch (request.getOperation()) {
                case "add":
                    result = calculator.add(request.getNum1(), request.getNum2());
                    break;
                case "subtract":
                    result = calculator.subtract(request.getNum1(), request.getNum2());
                    break;
                case "multiply":
                    result = calculator.multiply(request.getNum1(), request.getNum2());
                    break;
                case "divide":
                    result = calculator.divide(request.getNum1(), request.getNum2());
                    break;
                default:
                    response.put("error", "Unknown operation");
                    return ResponseEntity.badRequest().body(response);
            }
            response.put("result", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/exchange-rates")
    public ResponseEntity<Map<String, Double>> getExchangeRates() {
        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 1.0);
        rates.put("EUR", 0.92);
        rates.put("GBP", 0.78);
        rates.put("JPY", 151.72);
        rates.put("CAD", 1.37);
        rates.put("AUD", 1.53);
        rates.put("CHF", 0.91);
        rates.put("CNY", 7.25);
        return ResponseEntity.ok(rates);
    }
}

class Calculator {
    public double add(double a, double b) {
        return a + b;
    }
    
    public double subtract(double a, double b) {
        return a - b;
    }
    
    public double multiply(double a, double b) {
        return a * b;
    }
    
    public double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return a / b;
    }
}

class CalculationRequest {
    private String operation;
    private double num1;
    private double num2;
    
    public CalculationRequest() {
    }
    
    public String getOperation() {
        return operation;
    }
    
    public void setOperation(String operation) {
        this.operation = operation;
    }
    
    public double getNum1() {
        return num1;
    }
    
    public void setNum1(double num1) {
        this.num1 = num1;
    }
    
    public double getNum2() {
        return num2;
    }
    
    public void setNum2(double num2) {
        this.num2 = num2;
    }
}
