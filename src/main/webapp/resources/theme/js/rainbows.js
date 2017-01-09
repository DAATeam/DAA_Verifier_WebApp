
rainbows = {
	init: function(o) {
		$(function() {
			if (typeof o == "string")
				o = { 'selector': o };
	
			// TODO: Expand 3-letter hex codes into 6 letters
			
			// Default options
			var opt = {
				'selector': '.rainbows'
			};

			$.extend(opt, o);

			
			// Apply the gradient
			if (opt.from || opt.to) rainbows.gradient(opt);
			
			// Make snafucated
			if (opt.shadow || opt.highlight) rainbows.makeSnafucated(opt);
		});
	},
	
	gradient: function(o) {

		var opt = {
			'selector': '.rainbows',
			'from': '#ffffff',
			'to': '#000000'
		};

		$.extend(opt, o);

		$(opt.selector).each(function() {


			// Set the position to relative (Absolute should work too?)
			// TODO: Test this with absolute
			// if ($(this).css('position') != 'absolute')
		
			$(this).css({
				'position': 'relative',
				'overflow': 'hidden'
			});
			
			var h = $(this).height();
			var origH = $(this).css('height');
			
			var spans = [ ];

			var html;

			// Parse the colors into their R, G, and B constituents
			var f = [parseInt(opt.from.substring(1, 3), 16),
					parseInt(opt.from.substring(3, 5), 16),
					parseInt(opt.from.substring(5, 7), 16)],

				t = [parseInt(opt.to.substring(1, 3), 16),
					parseInt(opt.to.substring(3, 5), 16),
					parseInt(opt.to.substring(5, 7), 16)];


			// Store the initial contents in initHTML
			// in case they are needed later
			if (this.initHTML)
				html = this.initHTML;
			else
				html = this.innerHTML;

			this.initHTML = html;
			
			// Give it a bit of expanding space, in case the font is resized.
			for (var i = 0; i < h  * 1.0; i++) {
				// The ratio of the "from" to "to" colors
				var ratio = 1 - (h - Math.min(i, h)) / h;

				// Interpolate the three colors.
				var	c = [Math.round(f[0] * (1 - ratio) + t[0] * ratio),
						Math.round(f[1] * (1 - ratio) + t[1] * ratio),
						Math.round(f[2] * (1 - ratio) + t[2] * ratio)];


			//	d('C: ' + c);
				
				// TODO: rationale for -i? offset them down

				/*
					Two spans. The outer span is 1px tall and is positioned vertically
				*/

				// Push the spans into a temporary array
				spans.push('<span class="rainbow rainbow-' + i + '" style="color: rgb(' + c[0] + ',' + c[1] + ',' + c[2] + ');"><span style="top: ' + (-i) + 'px;">' + html + '</span></span>');

			}
			
			
			// Concatenate the spans and insert them into the document
			this.innerHTML = spans.join('');
			$(this).css('height', origH);
		});

	},

	/* make snafucated */
	makeSnafucated: function(o) {
		$(o.selector).each(function() {
			$(this).css('position', 'relative');

			// Get the original html
			if (this.initHTML)
				html = this.initHTML;
			else
				html = this.innerHTML;
			
			var hi = '', sh = '';
			
			// Create the wrappers, and let CSS handle the rest.
			if (o.shadow) sh = '<span class="rainbows-shadow">' + html + '</span>';
			if (o.highlight) hi = '<span class="rainbows-highlight">' + html + '</span>';
			
			// Append them to the element
			//	this.innerHTML = this.innerHTML + '<span class="rainbows-wrap">' + sh + hi + '</span>';
			this.innerHTML = this.innerHTML + sh + hi;
			
		
		});
	}
};

rainbows.init({
	highlight: true,
	shadow: true,
	from: '#ffffff',
	to: '#000000'
});