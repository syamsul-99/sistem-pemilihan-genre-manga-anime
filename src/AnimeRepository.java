import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AnimeRepository {

    private ArrayList<Anime> data = new ArrayList<>();

    // File disimpan FIX di folder project
    private File file = new File("anime.csv");

    public AnimeRepository(){
        ensureFileExist();
        load();

        // Jika kosong isi data default
        if(data.isEmpty()){
            save();
        }
    }

    // ================= CRUD ===================
    public ArrayList<Anime> getAll(){
        return data;
    }

    public void add(Anime a){
        data.add(a);
        save();
    }

    public void update(String id, String title, String genre, String date){
        for(int i = 0; i < data.size(); i++){
            if(data.get(i).getId().equals(id)){
                data.set(i, new Anime(id,title,genre,date));
                break;
            }
        }
        save();
    }

    public void delete(String id){
        data.removeIf(a -> a.getId().equals(id));
        save();
    }

    // ================= FILE SYSTEM ===================
    private void ensureFileExist(){
        try {
            if(!file.exists()){
                file.createNewFile();
            }
        } catch (Exception e){
            System.out.println("Gagal membuat file: " + e.getMessage());
        }
    }

    private void load(){
        data.clear();
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)
            );

            String line;
            while((line = br.readLine()) != null){
                if(line.trim().isEmpty()) continue;

                String[] s = line.split(",");
                if(s.length == 4){
                    data.add(new Anime(s[0],s[1],s[2],s[3]));
                }
            }
            br.close();

        } catch (Exception e){
            System.out.println("Load Error: " + e.getMessage());
        }
    }

    private void save(){
        try {
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)
            );

            for(Anime a : data){
                bw.write(a.getId()+","+a.getTitle()+","+a.getGenre()+","+a.getReleaseDate());
                bw.newLine();
            }

            bw.flush();
            bw.close();

            System.out.println("Data saved!");

        } catch (Exception e){
            System.out.println("Save Error: " + e.getMessage());
        }
    }
}
