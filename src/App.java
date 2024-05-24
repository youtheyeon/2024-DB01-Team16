public class App {
    public static void main(String[] args) {
        // DatabaseManager 초기화
        DatabaseManager databaseManager = new DatabaseManager();

        // DatabaseManager로 LibraryManager 초기화
        LibraryManager libraryManager = new LibraryManager(databaseManager);

        // LoginFrame 생성 및 필요한 의존성 전달
        LoginFrame loginFrame = new LoginFrame(libraryManager);
        loginFrame.setVisible(true);
    }
}
