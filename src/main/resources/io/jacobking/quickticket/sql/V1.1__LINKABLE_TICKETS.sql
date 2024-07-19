CREATE TABLE IF NOT EXISTS LINKED_TICKET
(
    ID        INTEGER PRIMARY KEY AUTOINCREMENT,
    TICKET_ID INTEGER NOT NULL REFERENCES TICKET (ID),
    LINKED_ID INTEGER NOT NULL REFERENCES TICKET (ID)
);

ALTER TABLE TICKET
    ADD COLUMN RESOLVED_BY TEXT DEFAULT NULL;

CREATE TABLE IF NOT EXISTS TICKET_CATEGORIES
(
    ID          INTEGER PRIMARY KEY AUTOINCREMENT,
    "NAME"      TEXT NOT NULL,
    COLOR       TEXT NOT NULL DEFAULT '#5DADD5',
    DESCRIPTION TEXT NOT NULL DEFAULT ''
);

INSERT INTO TICKET_CATEGORIES (ID, NAME)
VALUES (0, 'DEFAULT');

ALTER TABLE TICKET
    ADD COLUMN CATEGORY_ID INT NOT NULL DEFAULT 0;

CREATE TABLE IF NOT EXISTS INVENTORY
(
    ID               INTEGER PRIMARY KEY AUTOINCREMENT,
    ASSET_NAME       TEXT    NOT NULL,
    TOTAL_COUNT      INTEGER NOT NULL DEFAULT 0,
    LAST_ISSUED      INTEGER NULL REFERENCES EMPLOYEE (ID),
    LAST_ISSUED_DATE TEXT    NULL
);

CREATE TABLE IF NOT EXISTS INVENTORY_LOG
(
    ID            INTEGER PRIMARY KEY AUTOINCREMENT,
    ASSET_ID      INTEGER NOT NULL REFERENCES INVENTORY (ID),
    COUNT_AT_TIME INTEGER NOT NULL,
    EMPLOYEE_ID   INTEGER NOT NULL REFERENCES EMPLOYEE (ID),
    ISSUED_DATE   TEXT    NOT NULL
);

CREATE TABLE IF NOT EXISTS ALERTS
(
    ALERT_ID    INTEGER PRIMARY KEY AUTOINCREMENT,
    ALERT_NAME  TEXT    NOT NULL,
    ALERT_STATE BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO ALERTS(ALERT_NAME, ALERT_STATE)
VALUES ('DISABLE_ALERTS', FALSE),
       ('DISABLE_Announcements.get()., FALSE),
       ('INFORMATIONAL_ALERTS', TRUE),
       ('ERROR_ALERTS', TRUE),
       ('CONFIRMATION_ALERTS', TRUE),
       ('WARNING_ALERTS', TRUE),
       ('INFORMATION_Announcements.get()., TRUE),
       ('ERROR_Announcements.get()., TRUE),
       ('CONFIRMATION_Announcements.get()., TRUE),
       ('WARNING_Announcements.get()., TRUE);

DROP TABLE ALERT_SETTINGS;

ALTER TABLE EMAIL
    RENAME TO EMAIL_INTERFACE;

CREATE TABLE IF NOT EXISTS TICKET_INTERFACE
(
    DEFAULT_CATEGORY_ID   INTEGER NOT NULL REFERENCES TICKET_CATEGORIES (ID),
    DEFAULT_CATEGORY_NAME TEXT    NOT NULL DEFAULT 'DEFAULT',
    OPEN_POST_CREATION    BOOLEAN NOT NULL DEFAULT TRUE,
    DEFAULT_PRIORITY      TEXT    NOT NULL DEFAULT 'LOW',
    DEFAULT_STATUS        TEXT    NOT NULL DEFAULT 'OPEN'
);

CREATE TABLE IF NOT EXISTS STARTUP
(
    LATE_TICKET_ALERT       BOOLEAN NOT NULL DEFAULT FALSE,
    OPEN_LAST_TICKET_VIEWED BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS SECURITY
(
    USERNAME       TEXT    NOT NULL DEFAULT 'ADMIN',
    PASSWORD       TEXT    NOT NULL DEFAULT 'PASSWORD',
    LOGIN_REQUIRED BOOLEAN NOT NULL DEFAULT FALSE,
    LAST_LOGGED_IN TEXT NULL
);