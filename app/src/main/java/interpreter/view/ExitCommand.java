package interpreter.view;

import interpreter.model.value.StringValue;

public class ExitCommand extends Command{
    public ExitCommand(String key, String description) { super(key,description); }
    @Override public void execute() { System.exit(0); }
}
