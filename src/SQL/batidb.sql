CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Table for Clients
CREATE TABLE clients (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         name VARCHAR(255) NOT NULL,
                         address VARCHAR(255),
                         phone VARCHAR(20),
                         isProfessional BOOLEAN NOT NULL
);

-- Enumeration for Project Status
CREATE TYPE projectStatus AS ENUM ('INPROGRESS', 'COMPLETED', 'CANCELLED');

-- Table for Projects
CREATE TABLE projects (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          projectName VARCHAR(255) NOT NULL,
                          profitMargin DOUBLE PRECISION,
                          totalCost DOUBLE PRECISION,
                          status projectStatus,
                          client_id UUID,
                          FOREIGN KEY (client_id) REFERENCES Clients(id) ON DELETE CASCADE
);

-- Table for Components
CREATE TABLE components (
                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                            name VARCHAR(255) NOT NULL,
                            componentType VARCHAR(255),
                            vatRate DOUBLE PRECISION
);

-- Table for Materials
CREATE TABLE materials (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           component_id UUID,
                           unitCost DOUBLE PRECISION,
                           quantity DOUBLE PRECISION,
                           transportCost DOUBLE PRECISION,
                           qualityCoefficient DOUBLE PRECISION,
                           FOREIGN KEY (component_id) REFERENCES Components(id) ON DELETE CASCADE
);

-- Table for Labor
CREATE TABLE labor (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       component_id UUID,
                       hourlyRate DOUBLE PRECISION,
                       workHours DOUBLE PRECISION,
                       workerProductivity DOUBLE PRECISION,
                       FOREIGN KEY (component_id) REFERENCES Components(id) ON DELETE CASCADE
);

-- Table for Quotes
CREATE TABLE quotes (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        estimatedAmount DOUBLE PRECISION,
                        issueDate DATE,
                        isAccepted BOOLEAN,
                        project_id UUID,
                        FOREIGN KEY (project_id) REFERENCES Projects(id) ON DELETE CASCADE
);

ALTER TABLE projects ADD COLUMN surface DOUBLE PRECISION;

ALTER TABLE components ADD COLUMN project_id INT;
ALTER TABLE components ADD CONSTRAINT fk_project_id FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE;

ALTER TABLE quotes ADD COLUMN validatedDate DATE;

ALTER TABLE components ADD COLUMN project_id UUID;

ALTER TABLE components ADD CONSTRAINT fk_project_id FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE;

ALTER TABLE components DROP COLUMN project_id;
ALTER TABLE components ADD COLUMN project_id UUID;
ALTER TABLE components ADD CONSTRAINT fk_project_id FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE;
