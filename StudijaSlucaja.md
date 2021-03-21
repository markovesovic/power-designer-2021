# Mikroservisna arhitektura

Mikroservisi su tehnika razvoja softvera - varijanta servisno-orijentisanog arhitektonskog(SOA) stila koji struktuira aplikaciju kao kolekciju labavopovezanih servisa. U mikroservisnoj arhitekturi, servisi su sitno granulirani, a protokoli su jednostavni. Korist od podeleaplikacije na posebne, manje servise je u tome što se poboljšava modularnost, aplikacija je lakša za razumevanje, razvoj, testiranje i otpornija je na eroziju arhitekture. Ona omogućava paralelizaciju razvoja tako što dozvoljava manjim, autonomnim timovima da nezavisno razvijaju, isporučuju i skaliraju servise. Takođe, dozvoljava da se individualni servisi razvijaju kroz proces konstantnog refaktorisanja. Arhitektura zasnovana na mikroservisima omogućava kontinualnu isporuku proizvoda.

* Prednosti:
	1. Servisi u mikroservisnoj arhitekturi se mogu posebno isporučivati
	2. Nezavisnost u slučaju greške
	3. Servisi su lako zamenljivi
	4. Servisi se mogu implementirati korišćenjem različitih programskih jezika, baza, hardverskih i softverskih okruženja, u zavisnosti šta od toga najviše odgovara za implementaciju
	5. Obezbeđuje karakteristike koje doprinose skalabilinosti
	6. Servisi su ponovo upotrebljivi u nekim drugim projektima

* Mane:
	1. Servisi formiraju informativne barijere
	2. Inter-servisni pozivi preko mreže imaju veću cenu u smislu mrežne latentnosti (kašnjenja) i obrade poruka nego vreme unutar-procesnih poziva u okviru monolitnih servisnih procesa
	3. Testiranje i isporuka je komplikovanija
	4. Pomeranje odgovornosti između servisa je teže
	5. Gledanje veličine servisa kao primarnog strukturnog mehanizma može dovesti do previše servisa


# Servisno orijentisana arhitektura (SOA)

Servisno orijentisana arhitektura (SOA) je pristup razvoju softvera širom preduzeća koji koristi prednosti softverskih komponenti ili usluga za višekratnu upotrebu. Svaka usluga sastoji se od integracije koda i podataka potrebnih za izvršavanje određene poslovne funkcije - na primer, provera kredita kupca, prijavljivanje na web lokaciju ili obrada zahteva za hipoteku.

Servisni interfejsi pružaju “labavu vezu”, što znači da se mogu pozvati sa malo ili nimalo znanja o tome kako se integracija sprovodi ispod. Zbog ove “labave veze” i načina objavljivanja usluga, programeri mogu uštedeti vreme ponovnom upotrebom komponenata u drugim aplikacijama širom preduzeća.

* Prednosti:
	1. Nezavisna lokacija. Nije zapravo važno gde se usluge nalaze. Mogu se objaviti na jednom serveru ili na više različitih.
	2. Visoka upotrebljivost. Usluge se mogu ponovo koristiti bez obzira na njihovu raniju interakciju sa drugim uslugama. Ova ponovna upotrebljivost je moguća zahvaljujući SOA aplikacionoj infrastrukturi - kombinaciji malih samodovoljnih funkcija.
	3. Poboljšana skalabilnost. Možete lako prilagoditi sistem jer se više slojeva jedne usluge može istovremeno pokretati na različitim serverima.

* Mane:
	1. Velika unapred uložena sredstva.
	2. Veće opterećenje i produženo vreme odziva. U SOA-i, svaku interakciju između usluga prati puna validacija svih ulaznih parametara. Kao rezultat, opterećenje postaje izuzetno veliko, što zauzvrat produžava vreme odziva.
	3. Mnoštvo usluga. SOA ima vrlo poseban pristup radu. Usluge neprestano razmenjuju poruke tokom izvršavanja zadataka. Shodno tome, broj tih poruka postaje prevelik.

# Enterprise service bus (ESB)

ESB je posrednički alat koji se koristi za distribuciju posla između povezanih komponenti aplikacije. ESB-ovi su dizajnirani da pruže jednoobrazna sredstva za rad, nudeći aplikacijama mogućnost povezivanja na magistralu i pretplate na poruke na osnovu jednostavnih strukturnih pravila i pravila poslovne politike.

Kao takav, to je alat koji se koristi i u distribuiranom računanju i u integraciji komponenata. Najbolji način za razmišljanje o ovom alatu je da se vizualizuje kao skup prekidača koji mogu usmeriti poruku duž određene rute između komponenata aplikacije na osnovu sadržaja poruke i primene ili poslovnih politika.

* Prednosti:
	1. Olakšava promenu komponenti ili dodavanje dodatnih komponenti
	2. Obezbeđuje pogodno mesto za primenu bezbednosti i usklađenosti, logovanje i nadgledanje
	3. Može da obezbedi balansiranje opterećenja za poboljšanje performansi
	4. Može pružiti podršku pri prelasku u slučaju otkaza komponente ili resursa
* Mane:
	1. Dodavanje dodatnog koraka / poziva u protok usluge i dodavanje malog kašnjenja vremenu odziva
	2. Dodatni trošak za podešavanje ESB servera, VPN-ova i druge infrastrukture
	3. Sve usluge su usmerene na jedno mesto, imaće jednu tačku neuspeha
	4. Nema mogućnosti za proširivanje jedne usluge u ESB infrastrukturi

# Ključne razlike između SOA i mikroservisa

* **Komunikacija**: U arhitekturi mikro usluga svaka usluga se razvija nezavisno, sa svojim komunikacionim protokolom. Sa SOA-om, svaka usluga mora da deli zajednički komunikacioni mehanizam koji se naziva magistrala za preduzeća (ESB). ESB može postati jedna tačka neuspeha za celo preduzeće, a ako se jedna usluga uspori, može se izvršiti čitav sistem.

* **Interoperabilnost**: U interesu da stvari budu jednostavne, mikroservisi koriste lagane protokole za razmenu poruka poput HTTP / REST. SOA su otvoreniji za heterogene protokole za razmenu poruka.

* **Granularnost usluga**: Arhitekture mikroservisa sastoje se od visoko specijalizovanih usluga, od kojih je svaka dizajnirana da vrlo dobro radi jednu stvar. Usluge koje čine SOA-e, s druge strane, mogu se kretati od malih, specijalizovanih usluga do usluga širom preduzeća.

* **Brzina**: Koristeći prednosti deljenja zajedničke arhitekture, SOA-ovi pojednostavljuju razvoj i rešavanje problema. Međutim, ovo takođe čini da SOA rade sporije od arhitektura mikroservisa, što minimizira deljenje u korist dupliranja.

----

Za razvoj sistema za modelovanje, koristicemo mikroservisnu arhitekturu. Servisi se mogu razvijati nezavisno jedan od drugog u proizvoljnim programskim jezicima, sto doprinosi skalabilnosti sistema, a najvise odgovara nasem razvojnom timu.
