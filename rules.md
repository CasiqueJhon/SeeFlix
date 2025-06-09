# Guía de Desarrollo SeeFlix 📱

## Índice
1. [Estructura del Proyecto](#estructura-del-proyecto)
2. [Tecnologías y Versiones](#tecnologías-y-versiones)
3. [Arquitectura](#arquitectura)
4. [Guías de Implementación](#guías-de-implementación)
5. [Convenciones de Código](#convenciones-de-código)

## Estructura del Proyecto

### Organización de Paquetes
```
app/src/main/
├── java/
│   ├── data/
│   │   ├── local/          # Room Database
│   │   ├── remote/         # API Services
│   │   ├── repository/     # Repositories
│   │   └── models/         # Data models
│   ├── di/                 # Dependency Injection modules
│   ├── ui/
│   │   ├── features/       # Features organizadas por funcionalidad
│   │   ├── common/         # Componentes compartidos
│   │   └── utils/          # Utilidades de UI
│   └── domain/
│       ├── usecases/       # Casos de uso
│       └── models/         # Domain models
```

## Tecnologías y Versiones

### Android Configuration
- compileSdk: 33
- minSdk: 21
- targetSdk: 33

### Dependencias Principales
```gradle
// Versiones de librerías principales
kotlin_version = "actual en build.gradle"
hilt_version = "2.44.2"
retrofit_version = "2.9.0"
room_version = "2.2.5"
lifecycle_version = "2.5.1"
```

## Arquitectura

### MVVM + Clean Architecture
1. **Capa de Presentación (UI)**
   - Activities/Fragments
   - ViewModels
   - Estados de UI
   - ViewBinding para vistas

2. **Capa de Dominio**
   - Casos de uso
   - Modelos de dominio
   - Interfaces de repositorio

3. **Capa de Datos**
   - Implementaciones de repositorio
   - Fuentes de datos (Room, API)
   - Modelos de datos

### Inyección de Dependencias
- Utilizar Hilt para la inyección de dependencias
- Crear módulos en el paquete `di/`
- Un módulo por funcionalidad

## Guías de Implementación

### Nueva Feature Checklist
1. **Preparación**
   - [ ] Crear package en `ui/features/[nombre_feature]`
   - [ ] Definir modelos de datos necesarios
   - [ ] Crear casos de uso requeridos

2. **Implementación UI**
   - [ ] Crear Fragment/Activity
   - [ ] Implementar ViewBinding
   - [ ] Crear ViewModel
   - [ ] Definir estados de UI

3. **Implementación Datos**
   - [ ] Crear/Actualizar modelos Room si es necesario
   - [ ] Implementar servicios API si es necesario
   - [ ] Crear/Actualizar repositorio

4. **Testing**
   - [ ] Tests unitarios para ViewModel
   - [ ] Tests unitarios para casos de uso
   - [ ] Tests de integración si es necesario

### Manejo de Datos
1. **Room Database**
   ```kotlin
   @Entity
   data class TuModelo(
       @PrimaryKey val id: Int,
       // otros campos
   )
   ```

2. **API Calls con Retrofit**
   ```kotlin
   interface ApiService {
       @GET("endpoint")
       suspend fun getData(): Response<DataModel>
   }
   ```

3. **Repository Pattern**
   ```kotlin
   class Repository @Inject constructor(
       private val apiService: ApiService,
       private val database: AppDatabase
   )
   ```

## Convenciones de Código

### Nombrado
1. **Archivos**
   - ViewModels: `[Feature]ViewModel`
   - Fragments: `[Feature]Fragment`
   - Activities: `[Feature]Activity`
   - Use Cases: `[Acción]UseCase`

2. **Variables y Funciones**
   - Funciones: camelCase
   - Variables: camelCase
   - Constantes: SNAKE_CASE_MAÚSCULAS

### ViewModels
```kotlin
@HiltViewModel
class FeatureViewModel @Inject constructor(
    private val useCase: UseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
}
```

### Estados de UI
```kotlin
sealed class UiState {
    object Initial : UiState()
    object Loading : UiState()
    data class Success(val data: Data) : UiState()
    data class Error(val message: String) : UiState()
}
```

### Inyección de Dependencias
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object FeatureModule {
    @Provides
    @Singleton
    fun provideRepository(): Repository {
        // implementación
    }
}
```

## Recursos

### Colores y Estilos
- Usar los colores definidos en `colors.xml`
- Seguir el tema Material Design
- Mantener consistencia con `styles.xml`

### Imágenes
- Usar Glide para carga de imágenes
- Implementar placeholder y error images
```kotlin
Glide.with(context)
    .load(url)
    .placeholder(R.drawable.placeholder)
    .error(R.drawable.error)
    .into(imageView)
```

### YouTube Player
- Usar YouTubePlayerView para contenido de video
- Implementar lifecycle awareness

## Testing

### Unit Tests
```kotlin
@Test
fun `test description`() {
    // Given
    // When
    // Then
}
```

### UI Tests
- Usar Espresso para tests de UI
- Implementar tests de navegación
- Verificar estados de UI

## Manejo de Errores
1. Usar sealed classes para estados de error
2. Implementar manejo de errores consistente
3. Mostrar mensajes de error amigables al usuario

## Performance
1. Usar coroutines para operaciones asíncronas
2. Implementar paginación donde sea necesario
3. Optimizar uso de recursos

---

*Nota: Este documento debe ser actualizado cuando se realicen cambios significativos en la arquitectura o dependencias del proyecto.*

Última actualización: [Fecha actual] 