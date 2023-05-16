const { createApp } = Vue;

const app = createApp({
  data() {
    return {
      cards: [],
      debitCards: [],
      creditCards: [],
      isNewCard: false,
      cardType: "",
      cardColor: "",
      deletedCardNumbers: [] 
    }
  },
  created() {
    this.getCardsInfo();
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

          const deletedCardNumbers = JSON.parse(localStorage.getItem('deletedCardNumbers')) || [];
          this.cards = this.cards.filter(card => !deletedCardNumbers.includes(card.number));
          this.debitCards = this.debitCards.filter(card => !deletedCardNumbers.includes(card.number));
          this.creditCards = this.creditCards.filter(card => !deletedCardNumbers.includes(card.number));

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
        .then(() => window.location.href = "/Web/BigWing/index.html")
    },
    createCard() {
      axios.post('/api/clients/current/cards', "type=" + this.cardType.toUpperCase() + "&color=" + this.cardColor.toUpperCase(), { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
        .then(() => swal('Tarjeta creada con éxito'))
        .then(() => window.location.href = "/Web/cards.html")
        .catch(() => swal('No puedes crear mas tarjetas'))
    },
    eliminarTarjeta(numeroTarjeta) {
      axios
        .patch('/api/clients/current/cards/delete', `number=${numeroTarjeta}`)
        .then(() => {
          this.cards = this.cards.filter(card => card.number !== numeroTarjeta);
          this.debitCards = this.debitCards.filter(card => card.number !== numeroTarjeta);
          this.creditCards = this.creditCards.filter(card => card.number !== numeroTarjeta);
          console.log(this.cards);
          console.log(this.debitCards);
          console.log(this.creditCards);

          const deletedCardNumbers = JSON.parse(localStorage.getItem('deletedCardNumbers')) || [];
          deletedCardNumbers.push(numeroTarjeta);
          localStorage.setItem('deletedCardNumbers', JSON.stringify(deletedCardNumbers));
        })
        .catch((error) => {
          this.errorCard = true;
        });
    }


  }
});

app.mount("#app");
