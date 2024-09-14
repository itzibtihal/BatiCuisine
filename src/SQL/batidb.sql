CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Table for Clients
CREATE TABLE Clients (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         name VARCHAR(255) NOT NULL,
                         address VARCHAR(255),
                         phone VARCHAR(20),
                         isProfessional BOOLEAN NOT NULL
);

-- Enumeration for Project Status
CREATE TYPE projectStatus AS ENUM ('INPROGRESS', 'COMPLETED', 'CANCELLED');

-- Table for Projects
CREATE TABLE Projects (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          projectName VARCHAR(255) NOT NULL,
                          profitMargin DOUBLE PRECISION,
                          totalCost DOUBLE PRECISION,
                          status projectStatus,
                          client_id UUID,
                          FOREIGN KEY (client_id) REFERENCES Clients(id) ON DELETE CASCADE
);

-- Table for Components
CREATE TABLE Components (
                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                            name VARCHAR(255) NOT NULL,
                            componentType VARCHAR(255),
                            vatRate DOUBLE PRECISION
);

-- Table for Materials
CREATE TABLE Materials (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           component_id UUID,
                           unitCost DOUBLE PRECISION,
                           quantity DOUBLE PRECISION,
                           transportCost DOUBLE PRECISION,
                           qualityCoefficient DOUBLE PRECISION,
                           FOREIGN KEY (component_id) REFERENCES Components(id) ON DELETE CASCADE
);

-- Table for Labor
CREATE TABLE Labor (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       component_id UUID,
                       hourlyRate DOUBLE PRECISION,
                       workHours DOUBLE PRECISION,
                       workerProductivity DOUBLE PRECISION,
                       FOREIGN KEY (component_id) REFERENCES Components(id) ON DELETE CASCADE
);

-- Table for Quotes
CREATE TABLE Quotes (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        estimatedAmount DOUBLE PRECISION,
                        issueDate DATE,
                        isAccepted BOOLEAN,
                        project_id UUID,
                        FOREIGN KEY (project_id) REFERENCES Projects(id) ON DELETE CASCADE
);
