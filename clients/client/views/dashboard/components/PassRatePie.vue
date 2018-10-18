<template>
  <div>
    <highcharts :options="option" ref="highcharts"></highcharts>
  </div>
</template>
<script>

  export default {
    props: {
      passRate: {
        type: Number,
        required: true
      },
      title: {
        type: String,
        required: true
      },
      height: {
        type: Number
      }
    },

    computed: {
      option () {
        return {
          chart: {plotBackgroundColor: null, plotBorderWidth: 0, plotShadow: false, height: this.height},
          credits: { enabled: false },
          title: {text: this.passRate + '%<br>' + '<br>' + this.title, align: 'center', verticalAlign: 'middle', y: 5},
          tooltip: {enabled: false},
          plotOptions: {
            pie: {
              dataLabels: {enabled: false}
            }
          },
          series: [{type: 'pie', innerSize: '75%', name: this.title, data: [{name: 'Passed', y: this.passRate, color: '#4DD0E1'}, {name: 'Failed', y: 100 - this.passRate, color: '#FF3860'}]}]
        }
      }
    }
  }

</script>

<style>
</style>
