-- =====================================================
--  TRIGGERS FOR updated_at (MySQL automatically updates,
--  BUT this section ensures future compatibility)
-- =====================================================

-- WORKSPACES
DROP TRIGGER IF EXISTS trg_workspaces_update;
CREATE TRIGGER trg_workspaces_update
BEFORE UPDATE ON workspaces
FOR EACH ROW SET NEW.updated_at = CURRENT_TIMESTAMP;

-- BOARDS
DROP TRIGGER IF EXISTS trg_boards_update;
CREATE TRIGGER trg_boards_update
BEFORE UPDATE ON boards
FOR EACH ROW SET NEW.updated_at = CURRENT_TIMESTAMP;

-- TASK LISTS
DROP TRIGGER IF EXISTS trg_task_lists_update;
CREATE TRIGGER trg_task_lists_update
BEFORE UPDATE ON task_lists
FOR EACH ROW SET NEW.updated_at = CURRENT_TIMESTAMP;

-- TASKS
DROP TRIGGER IF EXISTS trg_tasks_update;
CREATE TRIGGER trg_tasks_update
BEFORE UPDATE ON tasks
FOR EACH ROW SET NEW.updated_at = CURRENT_TIMESTAMP;