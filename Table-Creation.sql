CREATE TABLE testUser(
	_id int primary key not null auto_increment,
    email varchar(30),
    firstName varchar(30),
    lastName varchar(30),
    phoneNumber int,
    userPass varchar(30),
    userPass2 varchar(30),
    userType varchar(30)
);

CREATE table temp_user(
	user_token int primary key default 0 not null unique,
    user_name varchar(30),
    user_email varchar(30),
    login_date datetime
);

CREATE TABLE logged_out_user(
    user_id INT,
    user_name varchar(30),
    user_email varchar(30),
    login_date datetime,
    logout_date datetime
);

CREATE TABLE property(
	property_ID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    property_Name VARCHAR(30) UNIQUE,
    property_Host VARCHAR(30),
    property_Address VARCHAR(200),
    property_Build_Date DATE,
    property_No_Bedrooms INT,
    property_No_Bathrooms INT,
    property_Price_Per_Night DOUBLE,
    is_Property_Available BOOLEAN,
    property_Amenities VARCHAR(200),
    host_ID INT,
    FOREIGN KEY (host_ID) REFERENCES testuser(_id)
);

CREATE TABLE reservations(
	reservation_ID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    property_ID INT,
    host_ID INT,
    guest_ID INT,
    property_Name VARCHAR(30),
    property_Host VARCHAR(30),
    guest_Name VARCHAR(30),
    is_Property_Available BOOLEAN,
    property_Start_Reservation DATE,
    property_End_Reservation DATE,
    FOREIGN KEY (property_ID) REFERENCES property(property_ID),
    FOREIGN KEY (host_ID) REFERENCES testuser(_id)
);

CREATE TABLE requests(
	request_ID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    property_ID INT,
    property_Name VARCHAR(30),
    property_Host VARCHAR(30),
    host_ID INT,
    guest_Name VARCHAR(30),
    guest_ID INT,
    property_Start_Reservation DATE,
    property_End_Reservation DATE,
    FOREIGN KEY (property_ID) REFERENCES property(property_ID),
    FOREIGN KEY (host_ID) REFERENCES testuser(_id)
);

CREATE TABLE messages(
	message_ID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    host_ID INT,
    guest_ID INT,
    host_Name VARCHAR(30),
    guest_Name VARCHAR(30),
    message_Subject VARCHAR(30),
    message_Content VARCHAR(255),
    reply VARCHAR(255) DEFAULT NULL,
    is_Addressed BOOLEAN DEFAULT 0,
    FOREIGN KEY (host_ID) REFERENCES testuser(_id),
    FOREIGN KEY (guest_ID) REFERENCES testuser(_id)
);