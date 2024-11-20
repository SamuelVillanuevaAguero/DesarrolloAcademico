/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilerias.busqueda;

import java.io.FileInputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.BorderStyle;

/**
 *
 * @author Samue
 */
public class clasePruebas {

    public static void escribirExcel(String rutaArchivo) {
        try (FileInputStream fileInputStream = new FileInputStream(rutaArchivo); Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheetAt(0); // Obtener la primera hoja

            // Estilo para datos normales  
            CellStyle estiloNormal = workbook.createCellStyle();
            estiloNormal.setAlignment(CellStyle.ALIGN_CENTER);
            estiloNormal.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            estiloNormal.setBorderTop(CellStyle.BORDER_THIN);
            estiloNormal.setBorderBottom(CellStyle.BORDER_THIN);
            estiloNormal.setBorderLeft(CellStyle.BORDER_THIN);
            estiloNormal.setBorderRight(CellStyle.BORDER_THIN);

            // Estilo para encabezados (en negrita)  
            CellStyle estiloEncabezado = workbook.createCellStyle();
            estiloEncabezado.cloneStyleFrom(estiloNormal);

            // Crear fuente en negrita  
            Font fuenteNegrita = workbook.createFont();
            fuenteNegrita.setBoldweight(Font.BOLDWEIGHT_BOLD);
            estiloEncabezado.setFont(fuenteNegrita);

            // Crear encabezados si no existen
            String[] encabezados = {"NO.", "NOMBRE DEL DOCENTE", "NO. CURSO", "F.DOCENTE", "A. DOCENTE"};
            if (sheet.getRow(9) == null) {
                Row headerRow = sheet.createRow(9);

                for (int i = 0; i < encabezados.length; i++) {
                    Cell cell = headerRow.createCell(i + 1);
                    cell.setCellValue(encabezados[i]);
                    cell.setCellStyle(estiloEncabezado);
                }
            }

            // Encontrar la siguiente fila vacía para agregar datos
            int rowNum = 10;
            while (sheet.getRow(rowNum) != null) {
                rowNum++;
            }

            // Llenar datos  
            Row row = sheet.createRow(rowNum);
            String[] datos = {"1", "Samuelin", "45", "Si", "No"};

            for (int i = 0; i < datos.length; i++) {
                Cell cell = row.createCell(i + 1);
                cell.setCellValue(datos[i]);
                cell.setCellStyle(estiloNormal);
            }

            // Ajustar ancho de columnas  
            for (int i = 0; i < encabezados.length; i++) {
                sheet.autoSizeColumn(i + 1);
            }

            // Guardar el archivo  
            try (FileOutputStream outputStream = new FileOutputStream(rutaArchivo)) {
                workbook.write(outputStream);
            }

            System.out.println("Datos añadidos exitosamente al archivo Excel");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Ejemplo de uso  
    public static void ejemploUso() {

        escribirExcel("C:\\Users\\Samue\\Desktop\\Gestion_de_Cursos\\Sistema\\informacion_modificable\\reporte.xlsx");
    }

    public static void main(String[] args) {
        ejemploUso();
    }
}
