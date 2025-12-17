# Telephone Billing Calculator

## Popis projektu

Tento projekt obsahuje jednoduchý technický modul pro výpočet částky k úhradě za telefonní účet na základě výpisu volání ve formátu CSV. Modul je implementován jako knihovna (JAR) a splňuje přesně definované rozhraní zadané v zadání.

Řešení je navrženo tak, aby bylo:

* snadno čitelné a pochopitelné,
* deterministické a testovatelné,

---

## Použité technologie

* **Java 17 (LTS)** – dlouhodobě podporovaná verze jazyka Java
* **Maven** – build a dependency management
* **JUnit 5** – jednotkové testy

Projekt **nepoužívá žádné externí knihovny** (kromě testovacího frameworku JUnit), veškerá logika je postavena výhradně na standardní Java API.

---

## Architektura a vrstvy

Projekt je záměrně rozdělen do jasných vrstev:

### 1. API vrstva

```java
com.phonecompany.billing.TelephoneBillCalculator
```

* Definuje veřejné rozhraní modulu
* Umožňuje snadnou výměnu implementace
* Odpovídá přesně zadání úkolu

### 2. Implementační vrstva

```java
com.phonecompany.billing.TelephoneBillCalculatorImpl
```

* Obsahuje veškerou výpočetní logiku
* Zodpovídá za:

  * parsování vstupu
  * identifikaci promo čísla
  * výpočet ceny hovoru po jednotlivých započatých minutách

### 3. Testovací vrstva

```java
src/test/java
```

* Jednotkové testy ověřující správnost výpočtu
* Pokrývá základní scénáře dle zadání

---

## Princip výpočtu

1. **Parsování vstupu**

   * CSV řádky jsou rozděleny na jednotlivé hovory
   * Čas je parsován pomocí `LocalDateTime`

2. **Promo akce**

   * Je nalezeno nejčastěji volané telefonní číslo
   * Při shodě více čísel je vybráno aritmeticky nejvyšší
   * Hovory na toto číslo jsou účtovány zdarma

3. **Účtování hovoru**

   * Cena je počítána po jednotlivých započatých minutách
   * Sazba se určuje podle času začátku dané minuty
   * Po překročení 5 minut hovoru se uplatňuje snížená sazba

Výsledná částka je vrácena jako `BigDecimal` s přesností na dvě desetinná místa.

---

## Spuštění projektu

### Build a testy

```bash
mvn clean test
```

---

## Poznámky k návrhu

* Iterace po minutách zajišťuje maximální přesnost a jednoduchou čitelnost
* Řešení je snadno rozšiřitelné (např. jiné sazby, více promo pravidel)
* Kód je psán s důrazem na srozumitelnost před mikro-optimalizacemi

---

## Autor

Technický test – implementace telefonního účtování
