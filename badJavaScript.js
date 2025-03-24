const fs = require('fs');
const http = require('http');

// Hardcoded credentials (Sensitive Data Exposure)
const dbConfig = {
    host: 'localhost',
    user: 'admin',
    password: 'password123' // Hardcoded password
};

// Command Injection
function executeCommand(userInput) {
    const { exec } = require('child_process');
    exec(`ls ${userInput}`, (err, stdout, stderr) => {
        if (err) {
            console.error(`Error: ${err.message}`);
            return;
        }
        console.log(`Output: ${stdout}`);
    });
}

// Insecure HTTP Request
function fetchData() {
    http.get('http://insecure-api.com/data', (res) => {
        let data = '';
        res.on('data', (chunk) => {
            data += chunk;
        });
        res.on('end', () => {
            console.log(data);
        });
    }).on('error', (err) => {
        console.error(`Error: ${err.message}`);
    });
}

// SQL Injection
function saveToDatabase(userInput) {
    const mysql = require('mysql');
    const connection = mysql.createConnection(dbConfig);

    const query = `INSERT INTO users (name) VALUES ('${userInput}')`; // Vulnerable to SQL Injection
    connection.query(query, (err, results) => {
        if (err) {
            console.error(`Error: ${err.message}`);
            return;
        }
        console.log('Data saved:', results);
    });

    connection.end();
}

// Insecure File Handling
function readSensitiveFile() {
    const filePath = '/etc/passwd'; // Accessing sensitive system file
    fs.readFile(filePath, 'utf8', (err, data) => {
        if (err) {
            console.error(`Error: ${err.message}`);
            return;
        }
        console.log(data);
    });
}

// Main function to trigger the vulnerabilities
function main() {
    const userInput = "'; DROP TABLE users; --"; // Malicious input for SQL Injection
    executeCommand(userInput); // Command Injection
    fetchData(); // Insecure HTTP Request
    saveToDatabase(userInput); // SQL Injection
    readSensitiveFile(); // Insecure File Handling
}

main();