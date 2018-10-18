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
      //  required: true,
        default: 50
      },
      title: {
        type: String,
      //  required: true,
        default: 'test'
      },
      data: {
        type: Array,
        default: []
      },
      drilldown: {
        type: Array,
        default: []
      },
      height: {
        type: Number
      }
    },

    computed: {
      option () {
        return {
          chart: {type: 'pie', plotBackgroundColor: null, plotBorderWidth: 0, plotShadow: false, height: this.height},
          credits: { enabled: false },
          title: {text: '', align: 'center', verticalAlign: 'middle', y: 5},
          tooltip: {enabled: true},
          plotOptions: {
            pie: {
              dataLabels: {enabled: true}
            }
          },
          series: [{
            type: 'pie',
            name: this.title,
            data: this.data
          }],
          drilldown: {
            series: this.drilldown
          }
        }
      }
    }
  }

</script>

<style>
</style>
