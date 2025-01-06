CREATE TABLE users (
user_id SERIAL PRIMARY KEY,
username VARCHAR(255) NOT NULL UNIQUE,
password VARCHAR(255) NOT NULL,
email VARCHAR(255) NOT NULL UNIQUE,
role VARCHAR(50) NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categories (
category_id SERIAL PRIMARY KEY,
name VARCHAR(255) NOT NULL,
description TEXT,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tasks (
task_id SERIAL PRIMARY KEY,
taskname VARCHAR(255) NOT NULL,
description TEXT,
status VARCHAR(50),
due_date TIMESTAMP,
priority VARCHAR(10) DEFAULT 'MEDIUM',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
user_id INT REFERENCES users(user_id) ON DELETE CASCADE,
category_id INT REFERENCES categories(category_id) ON DELETE SET NULL
);

CREATE TABLE notifications (
notification_id SERIAL PRIMARY KEY,
message TEXT NOT NULL,
read BOOLEAN DEFAULT FALSE,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
user_id INT REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE reminders (
reminder_id SERIAL PRIMARY KEY,
message TEXT NOT NULL,
scheduled_time TIMESTAMP NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
user_id INT REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE task_categories (
task_id INT REFERENCES tasks(task_id) ON DELETE CASCADE,
category_id INT REFERENCES categories(category_id) ON DELETE CASCADE,
PRIMARY KEY (task_id, category_id)
);

-- Add username to the INSERT statement
INSERT INTO users (user_id, username, email, password, role)
VALUES (1, 'testuser', 'test@example.com', 'password', 'USER');
