package Main;

public class User {
    private String username;
    private char[] password;
    private int Score  ;

    public User(String username, char[] password , int Score) {
        this.username = username;
        this.password = password;
        this.setScore(Score);
    }

    public String getUsername() {
        return username;
    }

    public char[] getPassword() {
        return password;
    }

	public int getScore() {
		return Score;
	}

	public void setScore(int score) {
		Score = score;
	}	
}
