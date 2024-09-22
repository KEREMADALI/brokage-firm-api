## Run Configurations
1. Open CMD in root
2. Run the command below
```
 ./gradlew bootRun
```
3. Go to the address below to reach swagger UI
```
http://localhost:8080/swagger-ui/index.html
```
4. Login details
```
UserName : user
Password : <Logged on terminal>
```

## Decisions
- Withdraw option checks "usableSize" attribute instead of "size"
- IBAN is not used, customerId is used instead
- CreatedAt and UpdatedAt fields can be set on DB scripts as well
- For 'Size' and 'Price' fields 'int' is used for simplicity

## Future Problems
- There should be a deleting mechanism for orders that have no use anymore


## Future Improvements
- Add input validation on controller
- Add controller unit tests
- Add input, output and endpoint explanations on Swagger