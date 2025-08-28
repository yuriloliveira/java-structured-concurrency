void main() {
    try (final StructuredTaskScope scope =  new StructuredTaskScope.ShutdownOnSuccess()) {
        StructuredTaskScope.Subtask<String> task1 = scope.fork(() -> "Task one");
        scope.join();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
