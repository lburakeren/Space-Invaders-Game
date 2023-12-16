package Main;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class UserList {
    
	private ArrayList<User> userList;
	private User currentUser ;


	public UserList() {
        userList = new ArrayList<>();
        loadUsersFromFile();
        currentUser = null ;
    }

    public void addUser(String username, char[] password , int Score) {
        User user = new User(username, password , Score);
        userList.add(user);

 
        try (FileWriter writer = new FileWriter("users.txt", true)) {
            writer.write(username + ":" + new String(password) + ":" + Score +"\n");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
    
    private void loadUsersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 3) {
                    String username = parts[0];
                    char[] password = parts[1].toCharArray();
                    int score = Integer.parseInt(parts[2]) ;
                    if(!searchUser(username,password)) {
                        User user = new User(username, password,score);
                        userList.add(user);
                    }
                } else {
                    System.out.println("Invalid line in file: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
    
    public boolean searchUser(String username, char[] password) {
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                if (Arrays.equals(user.getPassword(), password)) {
                	setCurrentUser(user);
                	return true;
                }
            }
        }
        return false;
    }
    
    public void updateUserScoreOnFile() {
        if (currentUser != null) {
            String fileName = "users.txt";

            try {
             
                String fileContent = new String(Files.readAllBytes(Paths.get(fileName)));

                String[] lines = fileContent.split("\n");
                StringBuilder updatedContent = new StringBuilder();
                for (String line : lines) {
                    String[] parts = line.split(":");
                    if (parts.length == 3) {
                        String username = parts[0];
                        char[] password = parts[1].toCharArray();
                        int score = Integer.parseInt(parts[2]);

                        if (username.equals(currentUser.getUsername()) && Arrays.equals(password, currentUser.getPassword())) {
                            score = currentUser.getScore(); 
                        }

                        updatedContent.append(username).append(":").append(new String(password)).append(":").append(score).append("\n");
                    } else {
                        System.out.println("Invalid line in file: " + line);
                    }
                }

             
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                    writer.write(updatedContent.toString());
                }
            } catch (IOException e) {
                System.out.println("Error reading/writing file: " + e.getMessage());
            }
        } else {
            System.out.println("No current user selected.");
        }
    }
    
    
    public ArrayList<User> getHighScores() {
        ArrayList<User> sortedList = new ArrayList<>(userList);
        sortedList.sort(Comparator.comparingInt(User::getScore).reversed());
        return sortedList;
    }


    public ArrayList<User> getUserList() {
		return userList;
	}

	public void setUserList(ArrayList<User> userList) {
		this.userList = userList;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public User getCurrentUser() {
		return currentUser;
	}
  
 

}
