CREATE TABLE file_access_log (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    log_date TEXT NOT NULL,
    file_path TEXT NOT NULL,
    access_count INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_file_access_log_date ON file_access_log(log_date);
CREATE INDEX idx_file_access_log_file_path ON file_access_log(file_path);
CREATE UNIQUE INDEX idx_file_access_log_date_path ON file_access_log(log_date, file_path);
