import React from 'react';
import logo1 from "./static/grad.png";
import logo2 from "./static/naukri.png";
import logo3 from "./static/iiitd.png";
import logo4 from "./static/vit.png";
import logo5 from "./static/snu.png";

export const BackedBySection: React.FC = () => {
  return (
    <section className="bg-white/5 py-16">
      <div className="max-w-7xl mx-auto px-4">
        <h2 className="text-center text-3xl font-bold text-violet-400 mb-8">Backed By</h2>
        <div className="overflow-hidden">
          <div className="flex animate-marquee gap-8">
            <img src={logo1} alt="Logo 1" className="h-12" />
            <img src={logo2} alt="Logo 2" className="h-12" />
            <img src={logo3} alt="Logo 3" className="h-12" />
            <img src={logo4} alt="Logo 4" className="h-12" />
            <img src={logo5} alt="Logo 5" className="h-12" />
            <img src={logo1} alt="Logo 1" className="h-12" />
            <img src={logo2} alt="Logo 2" className="h-12" />
            <img src={logo3} alt="Logo 3" className="h-12" />
            <img src={logo4} alt="Logo 4" className="h-12" />
            <img src={logo5} alt="Logo 5" className="h-12" />
          </div>
        </div>
      </div>
    </section>
  );
};
