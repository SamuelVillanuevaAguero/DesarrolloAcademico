package utilerias.principal;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Necesidadcapacitacion {

    public void generarArchivo() throws IOException {
        // Rutas de los archivos
        String rutaCapacitacion = "C:\\Users\\sebas\\OneDrive\\Escritorio\\Arhicvos Prueba\\Gestion_de_curso\\Archivos_importados\\Año\\Periodo\\programa_de_capacitacion(1).xlsx";
        String rutaNecesidades = "C:\\Users\\sebas\\OneDrive\\Escritorio\\Arhicvos Prueba\\Gestion_de_curso\\Archivos_importados\\Año\\Periodo\\listado_de_necesidad_de_acreditacion.xlsx";
        String rutaSalida = "C:\\Users\\sebas\\OneDrive\\Escritorio\\Arhicvos Prueba\\Gestion_de_curso\\Archivos_importados\\Año\\Periodo\\docentesRecomendable.xlsx";

        // Leer archivos
        List<String[]> listaCapacitacion = leerCapacitacion(rutaCapacitacion);
        Map<String, String[]> necesidades = leerNecesidades(rutaNecesidades);

        // Crear nuevo archivo basado en la comparación
        generarArchivoNuevo(listaCapacitacion, necesidades, rutaSalida);

        System.out.println("Archivo generado correctamente en: " + rutaSalida);
    }

    // Leer el archivo programa_de_capacitacion(1) y ordenar
    private static List<String[]> leerCapacitacion(String rutaArchivo) throws IOException {
        List<String[]> profesores = new ArrayList<>();
        FileInputStream fis = new FileInputStream(rutaArchivo);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue; // Saltar encabezado
            }
            String apellidoPaterno = leerCeldaComoTexto(row.getCell(4)); // Columna E
            String apellidoMaterno = leerCeldaComoTexto(row.getCell(5)); // Columna F
            String nombre = leerCeldaComoTexto(row.getCell(6)); // Columna G

            if (!nombre.isEmpty()) {
                profesores.add(new String[]{nombre, apellidoPaterno, apellidoMaterno});
            }
        }

        // Ordenar por nombre, apellido paterno, apellido materno
        profesores.sort(Comparator.comparing((String[] p) -> p[0])
                .thenComparing(p -> p[1])
                .thenComparing(p -> p[2]));

        workbook.close();
        fis.close();
        return profesores;
    }

    // Leer listado_de_necesidad_de_acreditacion
    private static Map<String, String[]> leerNecesidades(String rutaArchivo) throws IOException {
        Map<String, String[]> necesidades = new HashMap<>();
        FileInputStream fis = new FileInputStream(rutaArchivo);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue; // Saltar encabezado
            }
            String nombreCompleto = leerCeldaComoTexto(row.getCell(0)); // Columna A
            String fp = leerCeldaComoTexto(row.getCell(2)); // Columna C
            String ad = leerCeldaComoTexto(row.getCell(3)); // Columna D

            if (!nombreCompleto.isEmpty()) {
                necesidades.put(nombreCompleto, new String[]{fp, ad});
            }
        }

        workbook.close();
        fis.close();
        return necesidades;
    }

    // Generar el nuevo archivo basado en la comparación
    private static void generarArchivoNuevo(List<String[]> listaCapacitacion, Map<String, String[]> necesidades, String rutaSalida) throws IOException {
        Workbook workbookNuevo = new XSSFWorkbook();
        Sheet sheetNuevo = workbookNuevo.createSheet("Docentes Recomendable");

        // Encabezados
        Row headerRow1 = sheetNuevo.createRow(0);
        headerRow1.createCell(0).setCellValue("Nombre");
        headerRow1.createCell(1).setCellValue("Necesidad de capacitación detectada");
        headerRow1.createCell(2).setCellValue("Necesidad de capacitación detectada");

        Row headerRow2 = sheetNuevo.createRow(1);
        headerRow2.createCell(1).setCellValue("FP");
        headerRow2.createCell(2).setCellValue("AD");

        // Generador aleatorio
        Random random = new Random();

        // Llenar los datos
        int rowIndex = 2; // Comenzar en la tercera fila
        for (String[] profesor : listaCapacitacion) {
            String nombreCompleto = profesor[0] + " " + profesor[1] + " " + profesor[2];
            String[] necesidad = necesidades.getOrDefault(nombreCompleto, null);

            Row row = sheetNuevo.createRow(rowIndex++);
            row.createCell(0).setCellValue(nombreCompleto);

            // Determinar valores para FP y AD
            if (necesidad != null) {
                // Si aparece en la lista de necesidades
                row.createCell(1).setCellValue("Recomendable");
                row.createCell(2).setCellValue("Recomendable");
            } else {
                // Si no aparece, asignar aleatoriamente "Recomendable"
                row.createCell(1).setCellValue(random.nextBoolean() ? "Recomendable" : "");
                row.createCell(2).setCellValue(random.nextBoolean() ? "Recomendable" : "");
            }
        }

        // Guardar el archivo
        try (FileOutputStream fos = new FileOutputStream(rutaSalida)) {
            workbookNuevo.write(fos);
        }
        workbookNuevo.close();

        System.out.println("Archivo generado correctamente en: " + rutaSalida);
    }

    // Método auxiliar para leer una celda como texto
    private static String leerCeldaComoTexto(Cell cell) {
        if (cell == null) {
            return ""; // Celda vacía
        }
        switch (cell.getCellType()) {
            case 1:
                return cell.getStringCellValue().trim();
            case 2:
                return String.valueOf(cell.getNumericCellValue()).trim();
            case 3:
                return String.valueOf(cell.getBooleanCellValue()).trim();
            case 4:
                return cell.getCellFormula().trim();
            default:
                return ""; // Otros tipos (BLANK, ERROR, etc.)
        }
    }
}
