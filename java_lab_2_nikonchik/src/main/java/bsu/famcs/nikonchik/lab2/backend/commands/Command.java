package bsu.famcs.nikonchik.lab2.backend.commands;

public interface Command {
    enum CommandStatus {
        PENDING, EXECUTED, UNDONE, FAILED
    }
    void execute();
    CommandStatus getStatus();
}
