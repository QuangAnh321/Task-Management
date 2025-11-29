-- =====================================================
--  ENABLE STRICT MODE (recommended)
-- =====================================================
SET sql_mode = 'STRICT_ALL_TABLES';

-- =====================================================
--  DROP TABLES IN REVERSE ORDER (SAFE RE-RUN)
-- =====================================================
DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS task_lists;
DROP TABLE IF EXISTS boards;
DROP TABLE IF EXISTS workspaces;

-- =====================================================
--  WORKSPACES
-- =====================================================
CREATE TABLE workspaces (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_email VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP
);

-- Indexes
CREATE INDEX idx_workspaces_name ON workspaces (name);


-- =====================================================
--  BOARDS
-- =====================================================
CREATE TABLE boards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    workspace_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_boards_workspace FOREIGN KEY (workspace_id)
        REFERENCES workspaces(id)
        ON DELETE CASCADE
);

-- Indexes
CREATE INDEX idx_boards_workspace_id ON boards (workspace_id);
CREATE INDEX idx_boards_name ON boards (name);


-- =====================================================
--  TASK LISTS
-- =====================================================
CREATE TABLE task_lists (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    board_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_task_lists_board FOREIGN KEY (board_id)
        REFERENCES boards(id)
        ON DELETE CASCADE
);

-- Indexes
CREATE INDEX idx_task_lists_board_id ON task_lists (board_id);
CREATE INDEX idx_task_lists_name ON task_lists (name);


-- =====================================================
--  TASKS
-- =====================================================
CREATE TABLE tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_list_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_tasks_task_list FOREIGN KEY (task_list_id)
        REFERENCES task_lists(id)
        ON DELETE CASCADE
);

-- Indexes
CREATE INDEX idx_tasks_task_list_id ON tasks (task_list_id);
CREATE INDEX idx_tasks_name ON tasks (name);

