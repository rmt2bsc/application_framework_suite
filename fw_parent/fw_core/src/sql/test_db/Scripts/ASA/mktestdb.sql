//
//  This SQL file creates the framework test database.
//



CREATE DOMAIN company_name_t CHAR( 32 );
CREATE DOMAIN person_name_t CHAR( 20 );
CREATE DOMAIN person_title_t VARCHAR( 34 );
CREATE DOMAIN street_t CHAR( 30 );
CREATE DOMAIN city_t CHAR( 20 );
CREATE DOMAIN state_t CHAR( 16 );
CREATE DOMAIN country_t CHAR( 16 );
CREATE DOMAIN postal_code_t CHAR( 10 );
CREATE DOMAIN phone_number_t CHAR( 13 );

// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//     Create tables and grant permissions
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

CREATE TABLE Customers
(
        ID                    integer NOT NULL default autoincrement,
        Surname               person_name_t NOT NULL,
        GivenName             person_name_t NOT NULL,
        Street                street_t NOT NULL,
        City                  city_t NOT NULL,
        State                 state_t NULL,
        Country               country_t NULL,
        PostalCode            postal_code_t NULL,
        Phone                 phone_number_t NOT NULL,
        CompanyName           company_name_t NULL,
        CONSTRAINT CustomersKey PRIMARY KEY (ID)
);

COMMENT ON TABLE Customers IS 'customers of the sporting goods company';

CREATE TABLE Contacts
(
        ID                    integer NOT NULL,
        Surname               person_name_t NOT NULL,
        GivenName             person_name_t NOT NULL,
        Title                 person_title_t NULL,
        Street                street_t NULL,
        City                  city_t NULL,
        State                 state_t NULL,
        Country               country_t NULL,
        PostalCode            postal_code_t NULL,
        Phone                 phone_number_t NULL,
        Fax                   phone_number_t NULL,
	CustomerID            integer NULL default NULL,
        CONSTRAINT ContactsKey PRIMARY KEY (ID)
);

COMMENT ON TABLE Contacts IS 'names, addresses and telephone numbers of all people with whom the company wishes to retain contact information';

CREATE TABLE SalesOrders
(
        ID                    integer NOT NULL default autoincrement,
        CustomerID            integer NOT NULL,
        OrderDate             date NOT NULL,
        FinancialCode         char(2) NULL,
        Region                char(7) NULL,
        SalesRepresentative   integer NOT NULL,
        CONSTRAINT SalesOrdersKey PRIMARY KEY (ID)
);

COMMENT ON TABLE SalesOrders IS 'sales orders that customers have submitted to the sporting goods company';

CREATE TABLE SalesOrderItems
(
        ID                    integer NOT NULL,
        LineID                smallint NOT NULL,
        ProductID             integer NOT NULL,
        Quantity              integer NOT NULL,
        ShipDate              date NOT NULL,
        CONSTRAINT SalesOrderItemsKey PRIMARY KEY (ID, LineID)
);

COMMENT ON TABLE SalesOrderItems IS 'individual items that make up the sales orders';

CREATE TABLE Products
(
        ID                    integer NOT NULL,
        Name                  char(15) NOT NULL,
        Description           char(30) NOT NULL,
        Size                  char(18) NOT NULL,
        Color                 char(6) NOT NULL,
        Quantity              integer NOT NULL,
        UnitPrice             numeric(15,2) NOT NULL,
        Photo                 image NULL,
        CONSTRAINT ProductsKey PRIMARY KEY (ID)
);

COMMENT ON TABLE Products IS 'products sold by the sporting goods company';

CREATE TABLE FinancialCodes
(
        Code                  char(2) NOT NULL,
        Type                  char(10) NOT NULL,
        Description           char(50) NULL,
        CONSTRAINT FinancialCodesKey PRIMARY KEY (Code)
);

COMMENT ON TABLE FinancialCodes IS 'types of revenue and expenses that the sporting goods company has';

CREATE TABLE FinancialData
(
        Year                  char(4) NOT NULL,
        Quarter               char(2) NOT NULL,
        Code                  char(2) NOT NULL,
        Amount                numeric(9,0) NULL,
        CONSTRAINT FinancialDataKey PRIMARY KEY (Year, Quarter, Code)
);

COMMENT ON TABLE FinancialData IS 'revenues and expenses of the sporting goods company';

CREATE TABLE Departments
(
        DepartmentID          integer NOT NULL,
        DepartmentName        char(40) NOT NULL,
        DepartmentHeadID      integer NULL,
        CONSTRAINT DepartmentsKey PRIMARY KEY (DepartmentID)
);

COMMENT ON TABLE Departments IS 'contains the names and heads of the various departments in the sporting goods company';

CREATE TABLE Employees
(
        EmployeeID            integer NOT NULL,
        ManagerID             integer NULL,
        Surname               person_name_t NOT NULL,
        GivenName             person_name_t NOT NULL,
        DepartmentID          integer NOT NULL,
        Street                street_t NOT NULL,
        City                  city_t NOT NULL,
        State                 state_t NULL,
        Country               country_t NULL,
        PostalCode            postal_code_t NULL,
        Phone                 phone_number_t NULL,
        Status                char(2) NULL,
        SocialSecurityNumber  char(11) NULL,
        Salary                numeric(20,3) NOT NULL,
        StartDate             date NOT NULL,
        TerminationDate       date NULL,
        BirthDate             date NULL,
        BenefitHealthInsurance bit NULL,
        BenefitLifeInsurance  bit NULL,
        BenefitDayCare        bit NULL,
        Sex                   char(2) NULL,
        CONSTRAINT EmployeesKey PRIMARY KEY (EmployeeID)
);

COMMENT ON TABLE Employees IS 'contains information such as names, addresses, salary, hire date, and birthdays of the employees of the sporting goods company';

CREATE TABLE MarketingInformation 
(
	ID			integer NOT NULL,
	ProductID		integer NOT NULL,
	Description	        long varchar NULL,
	CONSTRAINT MarketingKey PRIMARY KEY (ID)
)

COMMENT ON TABLE MarketingInformation IS 'contains marketing information for the sporting goods company';

commit work;


// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//     Reload data
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

read adata\sales_o.sql;
read adata\sales_oi.sql;
read adata\contact.sql;
read adata\customer.sql;
read adata\fin_code.sql;
read adata\fin_data.sql;
read adata\product.sql;
read adata\dept.sql;
read adata\employee.sql;
read adata\marketinfo.sql;

commit work;

UPDATE Products
SET Photo=xp_read_file( 'adata\TankTop.jpg' )
WHERE Products.ID=300;

UPDATE Products
SET Photo=xp_read_file( 'adata\V-Neck.jpg' )
WHERE Products.ID=301;

UPDATE Products
SET Photo=xp_read_file( 'adata\CrewNeck.jpg' )
WHERE Products.ID=302;

UPDATE Products
SET Photo=xp_read_file( 'adata\CottonCap.jpg' )
WHERE Products.ID=400;

UPDATE Products
SET Photo=xp_read_file( 'adata\WoolCap.jpg' )
WHERE Products.ID=401;

UPDATE Products
SET Photo=xp_read_file( 'adata\ClothVisor.jpg' )
WHERE Products.ID=500;

UPDATE Products
SET Photo=xp_read_file( 'adata\PlasticVisor.jpg' )
WHERE Products.ID=501;

UPDATE Products
SET Photo=xp_read_file( 'adata\HoodedSweatshirt.jpg' )
WHERE Products.ID=600;

UPDATE Products
SET Photo=xp_read_file( 'adata\ZippedSweatshirt.jpg' )
WHERE Products.ID=601;

UPDATE Products
SET Photo=xp_read_file( 'adata\CottonShorts.jpg' )
WHERE Products.ID=700;
commit work;

// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//     Add foreign key definitions
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

ALTER TABLE SalesOrders
        ADD CONSTRAINT FK_SalesRepresentative_EmployeeID FOREIGN KEY (SalesRepresentative) REFERENCES Employees (EmployeeID);

ALTER TABLE SalesOrders
        ADD CONSTRAINT FK_FinancialCode_Code FOREIGN KEY (FinancialCode) REFERENCES FinancialCodes (Code) ON DELETE SET NULL;

ALTER TABLE SalesOrders
        ADD CONSTRAINT FK_CustomerID_ID FOREIGN KEY (CustomerID) REFERENCES Customers (ID);

ALTER TABLE SalesOrderItems
        ADD CONSTRAINT FK_ProductID_ID FOREIGN KEY (ProductID) REFERENCES Products (ID);

ALTER TABLE SalesOrderItems
        ADD CONSTRAINT FK_ID_ID FOREIGN KEY (ID) REFERENCES SalesOrders (ID) ON DELETE CASCADE;

ALTER TABLE Contacts
	ADD CONSTRAINT FK_CustomerID_ID FOREIGN KEY (CustomerID) REFERENCES Customers (ID);

ALTER TABLE FinancialData
        ADD CONSTRAINT FK_Code_Code FOREIGN KEY (Code) REFERENCES FinancialCodes (Code) ON DELETE CASCADE;

ALTER TABLE Departments
        ADD CONSTRAINT FK_DepartmentHeadID_EmployeeID FOREIGN KEY (DepartmentHeadID) REFERENCES Employees (EmployeeID) ON DELETE SET NULL;

ALTER TABLE Employees
        ADD CONSTRAINT FK_DepartmentID_DepartmentID FOREIGN KEY (DepartmentID) REFERENCES Departments (DepartmentID);

ALTER TABLE MarketingInformation
        ADD CONSTRAINT FK_MarketInfoID_ID FOREIGN KEY (ProductID) REFERENCES Products (ID);

// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//     Create indexes
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

CREATE INDEX IX_customer_name ON Customers
(
        Surname ASC,
        GivenName ASC
);

CREATE INDEX IX_product_name ON Products
(
        Name ASC
);
CREATE INDEX IX_product_description ON Products
(
        Description ASC
);
CREATE INDEX IX_product_size ON Products
(
        Size ASC
);
CREATE INDEX IX_product_color ON Products
(
        Color ASC
);

commit work;

// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//     Create text indexes
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

CREATE TEXT CONFIGURATION MarketingTextConfig FROM default_char;

ALTER TEXT CONFIGURATION MarketingTextConfig
   STOPLIST 'and the';

CREATE TEXT INDEX MarketingTextIndex ON MarketingInformation ( Description ) 
   CONFIGURATION MarketingTextConfig
   AUTO REFRESH EVERY 24 HOURS;

REFRESH TEXT INDEX MarketingTextIndex ON MarketingInformation;

commit work;

// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//     Create views (including materialized ones)
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

CREATE VIEW ViewSalesOrders
(ID,LineID,ProductID,Quantity,OrderDate,ShipDate,Region,SalesRepresentativeName)
AS
  SELECT i.ID,i.LineID,i.ProductID,i.Quantity,
         s.OrderDate,i.ShipDate,
         s.Region,e.GivenName||' '||e.Surname
    FROM SalesOrderItems AS i
        JOIN SalesOrders AS s
        JOIN Employees AS e
    WHERE s.ID=i.ID
        AND s.SalesRepresentative=e.EmployeeID;

CREATE MATERIALIZED VIEW EmployeeConfidential 
AS
  SELECT e.EmployeeID, e.DepartmentID, 
        e.SocialSecurityNumber, e.Salary, e.ManagerID, 
        d.DepartmentName, d.DepartmentHeadID
    FROM Employees AS e, Departments as d
    WHERE e.DepartmentID=d.DepartmentID;
    
    
REFRESH MATERIALIZED VIEW EmployeeConfidential;
ALTER MATERIALIZED VIEW EmployeeConfidential DISABLE;
	
commit work;