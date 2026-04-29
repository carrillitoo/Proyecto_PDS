package umu.pds.api;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ImportOption;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "umu.pds", importOptions = { ImportOption.DoNotIncludeTests.class })
public class ArchTests {

    @ArchTest
    static final ArchRule implementaciones_acaban_en_impl = classes()
            .that().areNotInterfaces()
            .should(new ArchCondition<JavaClass>("acabar en Impl si implementan interfaces locales") {
                @Override
                public void check(JavaClass item, ConditionEvents events) {
                    boolean implementsLocalInterface = item.getInterfaces().stream()
                            .anyMatch(i -> i.getName().startsWith("umu.pds"));
                    if (implementsLocalInterface && !item.getSimpleName().endsWith("Impl")
                            && !item.getSimpleName().endsWith("Test") && !item.getSimpleName().endsWith("Tests")) {
                        events.add(SimpleConditionEvent.violated(item,
                                item.getName() + " implementa una interfaz local pero no termina en Impl"));
                    }
                }
            });

    @ArchTest
    static final ArchRule ninguna_interfaz_acaba_en_impl = classes()
            .that().areInterfaces().should().haveSimpleNameNotEndingWith("Impl");

    @ArchTest
    static final ArchRule los_dto_acaban_en_dto = classes()
            .that().resideInAPackage("..dto..")
            .and().areNotEnums()
            .should().haveSimpleNameEndingWith("DTO")
            .orShould().haveSimpleNameEndingWith("Dto");

    @ArchTest
    static final ArchRule codigo_respeta_arquitectura_hexagonal = layeredArchitecture()
            .consideringAllDependencies()
            .layer("Domain").definedBy("..domain..")
            .layer("Application").definedBy("..application..")
            .layer("Adapters").definedBy("..adapters..")
            .optionalLayer("DTO").definedBy("umu.pds.dto..")
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Adapters", "DTO")
            .whereLayer("Application").mayOnlyBeAccessedByLayers("Adapters")
            .whereLayer("Adapters").mayNotBeAccessedByAnyLayer();

    @ArchTest
    static final ArchRule rest_controllers_en_adaptadores = classes()
            .that().haveSimpleNameEndingWith("Controller")
            .should().resideInAPackage("..adapters..")
            .orShould().resideInAPackage("..gui..");

    @ArchTest
    static final ArchRule max_25_metodos_publicos = classes()
            .that().areNotInterfaces()
            .should(new ArchCondition<JavaClass>("no tener más de 25 métodos públicos") {
                @Override
                public void check(JavaClass item, ConditionEvents events) {
                    long count = item.getMethods().stream()
                            .filter(m -> m.getModifiers().contains(JavaModifier.PUBLIC))
                            .count();
                    if (count > 25) {
                        events.add(SimpleConditionEvent.violated(item,
                                item.getName() + " tiene " + count + " métodos públicos"));
                    }
                }
            });
}
