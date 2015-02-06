package tv.mineinthebox.manco.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jnbt.ByteArrayTag;
import org.jnbt.CompoundTag;
import org.jnbt.NBTInputStream;
import org.jnbt.ShortTag;
import org.jnbt.StringTag;
import org.jnbt.Tag;

import tv.mineinthebox.manco.ManCo;
import tv.mineinthebox.manco.enums.LogType;
import tv.mineinthebox.manco.instances.Schematic;

/**
 * 
 * @author chasechocolate, sk89q
 * @param found this class on chasechocolate and parts on sk89q github repos
 * @param however this class is slightly modified, and will not be shipped with ManCo on bukkit dev because copying is not my thing.
 * @param this is to learn how I can read schematics in the future without the chance data values fail with negative values.
 *
 */
public class SchematicUtils {

	private final File baseSchematicsFile;
	private final List<Schematic> allSchematics = new ArrayList<Schematic>();

	public SchematicUtils(ManCo pl) {
		this.baseSchematicsFile = new File(pl.getDataFolder() + File.separator + "schematics");
	}
	
	public void initSchematics(){
		allSchematics.clear();
		for(File schematicFile : baseSchematicsFile.listFiles()){
			if(!(schematicFile.getName().startsWith("."))){
				Schematic schematic = loadSchematic(schematicFile);	
				if(schematic != null){
					allSchematics.add(schematic);
				}
			}
		}
	}

	public boolean doesExist(String name) {
		for(Schematic schem : getAllSchematics()) {
			if(schem.getName().equalsIgnoreCase(name)) return true;
		}
		return false;
	}

	public Schematic getByName(String name) {
		for(Schematic schem : getAllSchematics()) {
			if(schem.getName().equalsIgnoreCase(name)) return schem;
		}
		return null;
	}

	public List<Schematic> getAllSchematics(){
		return allSchematics;
	}

	public Schematic loadSchematic(File file){
		try{
			if(file.exists()){
				NBTInputStream nbtStream =  new NBTInputStream(new FileInputStream(file));
				CompoundTag compound = (CompoundTag) nbtStream.readTag();

				Map<String, Tag> tags = compound.getValue();

				Short width = ((ShortTag) tags.get("Width")).getValue();
				Short height = ((ShortTag) tags.get("Height")).getValue();
				Short length = ((ShortTag) tags.get("Length")).getValue();

				String materials = ((StringTag) tags.get("Materials")).getValue();

				byte[] blocksId = ((ByteArrayTag) tags.get("Blocks")).getValue();

				byte[] data = ((ByteArrayTag) tags.get("Data")).getValue();

				//ive found this at the github of the makers of worldedit so credits to them, it looks I was doing it wrong by using byte[] at the first place
				//and the datavalues are hex while a byte is not hex but a short can accept hex as 16, now the only thing to learn is the Tag AddBlocks and why it is used.
				short[] blocks = new short[blocksId.length];

				//need to look a bit over this.
				byte[] addId = new byte[0];
				if (tags.containsKey("AddBlocks")) {
					addId = ((ByteArrayTag) tags.get("AddBlocks")).getValue();
				}


				// Combine the AddBlocks data with the first 8-bit block ID
				for (int index = 0; index < blocksId.length; index++) {
					if ((index >> 1) >= addId.length) { // No corresponding AddBlocks index
						blocks[index] = (short) (blocksId[index] & 0xFF);
					} else {
						if ((index & 1) == 0) {
							blocks[index] = (short) (((addId[index >> 1] & 0x0F) << 8) + (blocksId[index] & 0xFF));
						} else {
							blocks[index] = (short) (((addId[index >> 1] & 0xF0) << 4) + (blocksId[index] & 0xFF));
						}
					}
				}
				//end of worldedit snippet.


				nbtStream.close();

				Schematic schematic = new Schematic(file.getName().replace(".schematic", ""), width, height, length, materials, blocks, data);

				return schematic;
			}
		} catch(Exception e){
			ManCo.log(LogType.SEVERE, "could not load this file: " + file.getName());
			e.printStackTrace();
		}

		return null;
	}
}
