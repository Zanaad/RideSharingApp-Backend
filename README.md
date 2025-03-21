# RideSharingApp-Backend

This project is a multi-tenant taxi management application designed for various taxi companies. It utilizes a shared database architecture, where all companies share the same database but are distinguished by a company_id field in relevant tables.

Since MySQL does not natively support multi-tenancy with an ORM, we have implemented Sequelize to enable Object-Relational Mapping (ORM) capabilities, making database interactions more efficient and structured.

Tech Stack

Frontend: React

Backend: Node.js

Database: MySQL

ORM: Sequelize

Database Structure

The database follows a shared schema model, where each tenant (taxi company) is identified by a unique company_id.