const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            cards: "",
            debitCards: [],
            creditCards: [],
            isNewCard: false,
            cardType: "",
            cardColor: "",
            id: (new URLSearchParams(location.search)).get("id")
        }
    },
    created() {
        this.getCardsInfo()
    },
    methods: {
        getCardsInfo() {
            axios.get("http://localhost:8080/api/clients/current")
                .then(response => {
                    this.cards = response.data.cards;
                    console.log(this.cards);
                    this.debitCards = this.cards.filter(card => card.type == "DEBITO");
                    console.log(this.debitCards);
                    this.creditCards = this.cards.filter(card => card.type == "CREDITO");
                    console.log(this.creditCards);
                    console.log(this.cardType);
            


                })
        },
        cardsByType(type) {
            if (this.cards.length < 1) {
                return []
            }
            return this.cards.filter(e => e.cardType == type)
        },
        newCard(type) {
            this.cardType = type
            this.isNewCard = true
        },
        logout() {
            axios.post('/api/logout')
                .then(() => window.location.href = "/Web/index.html")
        },
        createCard() {
            axios.post('/api/clients/current/cards', "type=" + this.cardType.toUpperCase() + "&color=" + this.cardColor.toUpperCase(), { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(() => swal('Tarjeta creada con éxito'))
                .then(() => window.location.href = "/Web/cards.html")
                .catch(() => swal('No puedes crear mas tarjetas'))
        },
        

    }
})

app.mount("#app");
