
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class CrearArchivoExcelDePrueba {
    public static void main(String[] args) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Datos");

        // Crear encabezados en la primera fila
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Director");
        headerRow.createCell(1).setCellValue("Coordinador");
        headerRow.createCell(2).setCellValue("Jefe de Departamento");
        headerRow.createCell(3).setCellValue("Total de Docentes");

        // Crear fila de datos en la segunda fila
        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue("Dr. Juan Perez");
        dataRow.createCell(1).setCellValue("Mtra. Ana Lopez");
        dataRow.createCell(2).setCellValue("Ing. Carlos Diaz");
        dataRow.createCell(3).setCellValue(25);

        // Guardar el archivo
        try (FileOutputStream fileOut = new FileOutputStream("DatosEjemplo.xlsx")) {
            workbook.write(fileOut);
            System.out.println("Archivo de prueba 'DatosEjemplo.xlsx' creado con éxito.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}