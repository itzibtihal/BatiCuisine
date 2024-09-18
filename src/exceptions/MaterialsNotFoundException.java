package exceptions;

import java.sql.SQLException;

public class MaterialsNotFoundException extends RuntimeException {
    public MaterialsNotFoundException() {
        super("Material not found");
    }

    public MaterialsNotFoundException(String message) {
        super(message);
    }

    public MaterialsNotFoundException(String errorFindingMaterial, SQLException e) {
    }
}