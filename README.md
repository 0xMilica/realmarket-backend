# propeler-backend

### How do I get set up? ###

#### Local profile
##### PostgreSQL in Docker

```sh
docker run -p 5432:5432 -e POSTGRES_PASSWORD=mysecretpassword -e POSTGRES_DB=propeler -d postgres
```

##### runing application from console
From git bash:
```sh
SPRING_PROFILES_ACTIVE=local gradle clean bootRun
```
or from powershell:
```sh
$env:SPRING_PROFILES_ACTIVE='local'
.\gradlew clean bootRun
```
If application is stuck running in background you can use this commands in powershell to kill it
```sh
netstat -ano | findstr :8080
taskkill /PID 210912 /F
```