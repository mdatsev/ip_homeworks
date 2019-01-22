const fs = require('fs')

for(let i = 1; i <= 4; i++) {
    const times = fs.readFileSync(i + '.txt').toString().split('\n').map(t => +t)
    console.log(`length: ${i} time: ${times.reduce((a, b) => a + b) / times.length / 10e9}s`)
}