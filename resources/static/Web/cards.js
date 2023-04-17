const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            cards: [],
            debitCards: [],
            creditCards: []
        }
    },
    created() {
        this.getCardsInfo()
    },
    methods: {
        getCardsInfo() {
            axios.get(`http://localhost:8080/api/clients/1`)
                .then(response => {
                    this.cards = response.data.cards;
                    console.log(this.cards);
                    this.debitCards = this.cards.filter(card => card.type == "DEBITO");
                    console.log(this.debitCards);
                    this.creditCards = this.cards.filter(card => card.type == "CREDITO");
                    console.log(this.creditCards);


                })
        },
    }
})

app.mount("#app");
