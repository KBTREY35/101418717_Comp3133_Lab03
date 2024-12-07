print('START');

db = db.getSiblingDB('booking-service');
db.createUser({
    user: "admin",
    pwd: "password",
    roles: [{ role: "root", db: "admin" }]
});


db.createCollection('user');

print('END');

