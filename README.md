# propeler-backend

### How do I get set up? ###

#### Local profile
##### PostgreSQL in Docker

```sh
docker run -p 5432:5432 -e POSTGRES_PASSWORD=mysecretpassword -e POSTGRES_DB=propeler -d postgres
```