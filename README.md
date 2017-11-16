# provisioning-service
Restful api for user registration, verify account by email, fetch details on verification link access

- Finished: 
	1. Create a restful api for user registration with following fields 
	-	Name
	-	Email
	-	Pincode
	On successful registration UserId will be generated.

	![Alt text](/package/src/site/resources/docs/DB-UserCreation.jpg?raw=true "User creation- with random ID")

	2. Create a restful api to get login for registered user. 
	-	Call login api with UserId
	-	Send login link (http-link) to his registered email.
	-	Login-link expires in 15 min.

	![Alt text](/package/src/site/resources/docs/EmailVerification.png?raw=true "Email verification- with ID")

	3. User opens given login-link. If login-link is valid and not expired. Show user details.
	-	Name
	-	Email
	-	Pincode
	You can just represent data as JSON.

- TODO: 
	4. Create a restful api to get current temperature by pincode
	-	Use https://openweathermap.org/current api
	-	Save temperature-info into database.
	-	If temperature-info already exist in database and it is less than 30-sec old. Then give saved-value else refresh data (call open-weather api) then return new value.

	5. As in step 3, Instead of showing user details. Redirect user to show current-temperature for his/her pincode (saved at registration time).

	6. Write a schedule which updates temperature-info in database every minute for all saved records older more than 1 minute.

