package example;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import org.gnome.gdk.Event;
import org.gnome.glib.Glib;
import org.gnome.gtk.Builder;
import org.gnome.gtk.Entry;
import org.gnome.gtk.Gtk;
import org.gnome.gtk.ListStore;
import org.gnome.gtk.TreeIter;
import org.gnome.gtk.Widget;
import org.gnome.gtk.Window;
import org.gnome.gtk.Window.DeleteEvent;

import com.itextpdf.text.DocumentException;

import org.gnome.gtk.DataColumnString;
import org.gnome.gtk.ComboBox;
import org.gnome.gtk.Button;
import org.gnome.gtk.CellRendererText;
import org.gnome.gtk.Button.Clicked;


public class Main {
	
	Builder BUILDER;
	Window WINDOWS;
	Entry NAME, LASTNAME, EMAIL;
	Entry ADDRESS, PHONE, DAY, YEAR;
	ComboBox MONTH;
	ComboBox SEX;
	Button bttnPDF, bttnTXT;
	ListStore listStoreSex, listStoreMonth;
	DataColumnString sexId, monthId;
	CellRendererText sex_cellRendererId, month_cellRendererId;
	DataColumnString column;
	TreeIter row;
	String sex[] = {"Hombre","Mujer"};
	String month[] = {"Enero","Febrero","Marzo","Abril","Mayo","Junio",
			"Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
	
	
	public Main() {
		
		try {
			//String HOME = System.getProperty("user.home");
			String FOLDER_DIR="src/main/java/gui";
			
			BUILDER = new Builder();
			BUILDER.addFromFile(FOLDER_DIR+"/gui.glade");
			
		} catch(FileNotFoundException e) {
			
			System.err.println(e.getMessage());
			
		} catch(ParseException e) {
			
			System.err.println(e.getMessage());
			
		}
		
		startObjects();
		
		WINDOWS.showAll();
		
	}
	
	public void startObjects() {
		
		//Ventana principal
		WINDOWS = (Window) BUILDER.getObject("ID_Wformulario");
		
		//Cajas de texto
		NAME 		= (Entry) BUILDER.getObject("ID_nombre");
		LASTNAME 	= (Entry) BUILDER.getObject("ID_apellido");
		EMAIL 		= (Entry) BUILDER.getObject("ID_email");
		ADDRESS 	= (Entry) BUILDER.getObject("ID_direccion");
		PHONE 		= (Entry) BUILDER.getObject("ID_telefono");
		DAY 		= (Entry) BUILDER.getObject("ID_dia");
		YEAR 		= (Entry) BUILDER.getObject("ID_anio");
		
		//ComboBoxText
		
		MONTH = (ComboBox) BUILDER.getObject("ID_mes");
		
		listStoreMonth = new ListStore(new DataColumnString[]{
	    		
	    		monthId = new DataColumnString(),
		});
		
		MONTH.setModel(listStoreMonth);
		
		month_cellRendererId = new CellRendererText (MONTH);
		
		month_cellRendererId.setText(monthId);
		
		
		for (int i = 0; i < month.length; i++) {
			
			row = listStoreMonth.appendRow();
			listStoreMonth.setValue(row, monthId, month[i]);
		}
		
		////////////////////////////////////////////////////
		
		SEX = (ComboBox) BUILDER.getObject("ID_sexo");
		
		
		 listStoreSex = new ListStore(new DataColumnString[]{
		    		
		    		sexId = new DataColumnString(),
		 });
		 
		 SEX.setModel(listStoreSex);
		
		 sex_cellRendererId = new CellRendererText(SEX);	
		 
		 sex_cellRendererId.setText(sexId);
		 
		 
		 for (String string : sex) {
			 
			 row = listStoreSex.appendRow();
			    listStoreSex.setValue(row, sexId, string);
		}
		 	
		//Botones
		bttnPDF = (Button) BUILDER.getObject("ID_btt_PDF");
		bttnTXT = (Button) BUILDER.getObject("ID_btt_TXT");
		
		
		//Conexion con metodos
		WINDOWS.connect(on_window_destroy());
		bttnPDF.connect(on_button_pdf());
		bttnTXT.connect(on_button_txt());
			
	}
	
	private DeleteEvent on_window_destroy() {
		return new Window.DeleteEvent() {
			
			@Override
			public boolean onDeleteEvent(Widget arg0, Event arg1) {
				Gtk.mainQuit();
				return false;
			}
		};
		
	}
	
	private Clicked on_button_pdf() {
		return new Button.Clicked() {
			
			@Override
			public void onClicked(Button arg0) {
				
				String name = String.format("Nombre: %s %s \n",NAME.getText().toUpperCase(),LASTNAME.getText().toUpperCase());
				String email = String.format("Correo electronico: %s \n",EMAIL.getText());
				String address = String.format("Direccion: %s \n",ADDRESS.getText().toUpperCase());
				String phone = String.format("Telefono: %s \n",PHONE.getText());
				String date = null;
				String sx = null;
				
				try {
					
					if(MONTH.getActive()!=-1) {
						
						date = String.format("Fecha de nacimiento: %s/%s/%s \n",DAY.getText(),month[MONTH.getActive()].toUpperCase(),YEAR.getText());
					}
					
					if(SEX.getActive()!=-1) {
						
						sx = String.format("Sexo : "+sex[SEX.getActive()]);
					}
					
				}catch(ArrayIndexOutOfBoundsException e) {
					
					System.out.println(e.getMessage());
				}
	
				
				GeneratePDFiText generatePDFFileIText = new GeneratePDFiText();
			    try {
					generatePDFFileIText.createPDF(name, email, address, phone, date, sx);
				} catch (DocumentException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}
	
	private Clicked on_button_txt() {
		return new Button.Clicked() {
			
			@Override
			public void onClicked(Button arg0) {
				
				NAME.setText("");
				LASTNAME.setText("");
				EMAIL.setText("");
				ADDRESS.setText("");
				PHONE.setText("");
				DAY.setText("");
				YEAR.setText("");
				
			}
		};
	}
	
	
	public static void main(String[] args) {
		
		Glib.setProgramName("Formulario PDF");
		Gtk.init(args);
		new Main();
		Gtk.main();

	}

}
