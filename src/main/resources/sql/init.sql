-- Create the case_study_module_4 database
CREATE DATABASE IF NOT EXISTS case_study_module_4;
USE case_study_module_4;

-- Create the note_types table
CREATE TABLE IF NOT EXISTS note_types (
                                          id INT AUTO_INCREMENT PRIMARY KEY,
                                          name VARCHAR(50) NOT NULL
    );

-- Create the notes table
CREATE TABLE IF NOT EXISTS notes (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     title VARCHAR(255) NOT NULL,
    content TEXT NOT NOT NULL,
    type_id INT,
    FOREIGN KEY (type_id) REFERENCES note_types(id)
    );

-- Insert initial data into note_types
INSERT INTO note_types (name) VALUES ('Personal'), ('Work'), ('Study');